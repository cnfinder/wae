package cn.finder.wae.service.rest.resource.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.comm.JsonUtil;
/**
 *@author wu hualong
 */
public abstract class BaseQueryController {

	private final static Logger logger=Logger.getLogger(BaseQueryController.class);
	
	protected QueryCondition<Object[]> queryCondition=new QueryCondition<Object[]>();
	
	
	public BaseQueryController()
	{
		
	}
	
	public void processWhereCondition(HttpServletRequest request){
		
		long searchShowTableConfigId =-1;
		
		try{
			searchShowTableConfigId= Long.valueOf(request.getParameter("searchShowTableConfigId"));
		}
		catch(Exception e)
		{
			return ;
		}
		
		String whereCondition = request.getParameter("whereCondition");
		
		logger.debug("通用搜索参数:"+whereCondition);
		
		Map<String,Object> dataMap = null;
		if(!StringUtils.isEmpty(whereCondition))
		{
			dataMap=JsonUtil.getMap4Json(whereCondition);
			StringBuffer sb = new StringBuffer();
			if(dataMap!=null){
				//设置 whereCluster 和 wherepParameterValues
				
				//List<ShowDataConfig>  showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showtableConfigId);
				List<ShowDataConfig>  showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(searchShowTableConfigId);
				
				Set<Entry<String, Object>> set_entry =dataMap.entrySet();
				Iterator<Entry<String, Object>> ite=set_entry.iterator();
				
				//保存 wherepParameterValues
				List<Object> whereValues = new ArrayList<Object>();
				
				sb.append(" 1=1 ");
				
				while(ite.hasNext())
				{
					Entry<String,Object> entry = ite.next();
					String name = entry.getKey();
					Object value = entry.getValue();
					
					int dataType=11;
					
					ShowDataConfig  showDataConfig = null;
					for(ShowDataConfig sdf:showDataConfigs)
					{
						if(sdf.getFieldName().equals(name))
						{
							dataType = sdf.getDataType();
							showDataConfig= sdf;
							break;
						}
					}
					
					if(dataType!=14 && showDataConfig.getShowType() !=29)
					{
						if(StringUtils.isEmpty(""+value))
						{
							continue;
						}
					}
					
					//根据 dataType 判断数据类型
					
					switch(dataType)
					{
						case 11:
							//字符串 做 like 
							
							sb.append(" and ");
							sb.append(name).append(" like ? ");
							whereValues.add("%"+value+"%");
							break;
						case 12:
						case 15:
					//	case 6:
							//数字类型   =
							//如果是 showType为区间显示，那么使用 <= 否则  >=
							if(showDataConfig.getShowType() ==29)
							{
								if(!StringUtils.isEmpty((String)value))
								{
									sb.append(" and ");
									sb.append(name).append(" >= ? ");
									whereValues.add(value);
								}
								
								
								ite.hasNext();
								entry = ite.next();
								value = entry.getValue();
								
								if(!StringUtils.isEmpty((String)value))
								{
									sb.append(" and ");
									sb.append(name).append(" <= ? ");
									whereValues.add(value);
								}
								
							}
							else{
								sb.append(" and ");
								sb.append(name).append("=?");
								whereValues.add(value);
								
								
							}
							
							break;
						
							
						case 14:
							//日期   between and 
							
							Date startDate = Common.parseDate3((String)value);
							if(startDate!=null)
							{
								sb.append(" and ");
								sb.append(name).append(" >= ?");
								whereValues.add(startDate);
							}
							
							
							ite.hasNext();
							entry = ite.next();
							value = entry.getValue();
							
							Date endDate = Common.parseDate3((String)value);
							if(endDate!=null)
							{
								sb.append(" and ");
								sb.append(name).append(" <= ?");
								whereValues.add(endDate);
							}
							
							
							break;
						
					}
					
					
					
					
				}
				
				logger.debug("通用搜索WhereSQL:"+sb.toString());
				queryCondition.setWhereCluster(sb.toString());
				queryCondition.setWherepParameterValues(whereValues.toArray());
			}
		}
		
	}
	public void parserRequestParams(HttpServletRequest request){
		
		processWhereCondition(request);
		
		String sortField = request.getParameter("sortField");
		String sortDirect = request.getParameter("sortDirect");
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortDirect)){
			sortMap.put(sortField, sortDirect);
		}
		
		int pageIndex = 1;
		int pageSize = 10;
		try{
			pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		}
		catch(Exception e){
			
		}
		
		try{
			 pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		catch(Exception e){
			
		}
		queryCondition.setOrderBy(sortMap);
		
		queryCondition.setPageIndex(pageIndex);
		queryCondition.setPageSize(pageSize);
	}
	
	
}
