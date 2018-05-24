package cn.finder.wae.queryer.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.http.HttpUtil;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;


/***
 *  定时拉取WEB服务数据  (应由 任务计划执行) JSON格式
 * @author jcl
 *
 */
public class PullDataWebServiceCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(PullDataWebServiceCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into PullDataWebServiceCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		@SuppressWarnings("unchecked")
		MapParaQueryConditionDto<String, Object> mapPara =(MapParaQueryConditionDto<String, Object>)condition;
		TaskPlan taskPlan =  (TaskPlan)mapPara.get("taskplan");
		
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		
		
		//获取服务地址
		String serviceUrl="";
		String dataNav = taskPlan.getString("data").toString();
		try {
			serviceUrl = URLDecoder.decode(taskPlan.getString("serviceUrl"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
	   
	   List<ShowDataConfig> showdataConfigs = showTableConfig.getShowDataConfigs();
	   
	    try {
			String resString="";
			try {
				resString = HttpUtil.get(serviceUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(StringUtils.isEmpty(resString)){
				
				qr.setPageIndex(condition.getPageIndex());
				qr.setPageSize(condition.getPageSize());
				return qr;
				
			}
			
			JSONObject respJSONObject = JSONObject.fromObject(resString);
			
		/*	JSONObject queryResultJSONObject = respJSONObject.getJSONObject("queryResult");
			JSONArray dataArr = queryResultJSONObject.getJSONArray("resultList");*/
			
			JSONObject prevJSONObject = respJSONObject;
			JSONArray dataArr = null;
			String[] keys = StringUtils.split(dataNav, ".");
			
			int dataArrIndex = keys.length-1;
			
			for(int t=0;t<keys.length;t++){
				
				String key_name = keys[t];
				
				if(t==dataArrIndex){
					dataArr=prevJSONObject.getJSONArray(key_name);
				}
				else
				{
					prevJSONObject = prevJSONObject.getJSONObject(key_name);
				}
				
			}
			
			
			
			
			
			
			
			//1.进行 字段对应处理
			// 建立 接口数据 属性的键  《-》  接口别名 对应  即 : his_test_presdata 的 field_name 对应键 然后映射别名
			
			for(int i=0;i<dataArr.size();i++){
			   JSONObject itemData=dataArr.getJSONObject(i);
				
			   Map<String,Object> dataItem = new HashMap<String,Object>();
			   
			   for(ShowDataConfig sdf:showdataConfigs){
				   
				   String fieldName = sdf.getFieldName();
				   String fieldNameAlias = sdf.getFieldNameAlias();
				   
				   if(itemData.containsKey(fieldName)){
					   //接口记录中 包含 配置列
					   Object fieldValue = itemData.get(fieldName);
					   
					   dataItem.put(fieldNameAlias, fieldValue);
				   }
			   }
			   
			   dataList.add(dataItem);
			   
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	  //2. 设置到ResultList
	    qr.setResultList(dataList);
	    
	    
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount((long)dataList.size());
		
		
		
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
