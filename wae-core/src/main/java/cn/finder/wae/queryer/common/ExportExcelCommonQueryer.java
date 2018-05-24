package cn.finder.wae.queryer.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.dto.StringParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;


/***
 *
 * 
 * 导出excel文档
 * @author wu hualong
 *
 */
public class ExportExcelCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(ExportExcelCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.info("=====go into ExportExcelCommonQueryer======");
		
		logger.info("==showTableConfigId:"+showTableConfigId);
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(10000000);
	    }
		StringParaQueryConditionDto dto = (StringParaQueryConditionDto)condition;
		
		if(condition!=null)
		{
			logger.info("==Sql:"+condition.getSql());
			logger.info("==WhereCluster:"+condition.getWhereCluster());
			logger.info("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.info("==pageIndex:"+condition.getPageIndex());
			logger.info("==pageSize:"+condition.getPageSize());
		}
		
		
		
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		
		
		
		
	    final List<ShowDataConfig> showDataConfigsTotal =showTableConfig.getShowDataConfigs();
	    
	    final List<ShowDataConfig> showDataConfigs =new ArrayList<ShowDataConfig>();
	    
	    
	    StringBuffer sql_sb =new StringBuffer();
	    
	    StringBuffer sql_cnt_sb = new StringBuffer();
	    
	    
	    // "id 编号,name 名称"
	    StringBuffer fields_sb =new StringBuffer();
	    
	    // where 后面的条件
	    StringBuffer where_sb =new StringBuffer();
	    
	    
	   final  List<String> filedNames=new ArrayList<String>();
	   
	   
	   logger.info("===export fields:"+dto.getStringPara());
	   
	   JSONArray ja =JSONArray.fromObject(dto.getStringPara());
		
		
		//获取到 表格数据  ,文件名称 
		
		List<String>  headerTextList = new ArrayList<String>();
		
		for(int i=0;i<ja.size();i++){
			JSONObject jo = (JSONObject)ja.get(i);
			headerTextList.add((String)jo.get("header"));
			
		}
	   
	    
    
    	for(String header_text:headerTextList){
    		for(ShowDataConfig df:showDataConfigsTotal)
    	    {
    			if(header_text.equalsIgnoreCase(df.getFieldNameAlias()))
    			{
    				filedNames.add(df.getFieldName()+" "+df.getFieldNameAlias());
    				showDataConfigs.add(df);
    			}
    			
    	    }
    	}
	    	
	    
	    fields_sb.append(StringUtils.join(filedNames, ','));
	    
	    
	    if(!StringUtils.isEmpty(condition.getWhereCluster()))
	    {
	    	 where_sb.append(condition.getWhereCluster());
	    }
	    
	    if(StringUtils.isEmpty(condition.getWhereCluster()))
	    {
	    	 where_sb.append(showTableConfig.getSqlConfig());
	    }
	    else{
	    	 where_sb.append(" "+showTableConfig.getSqlConfig());
	    }
	    
	    sql_sb.append("select ").append(fields_sb+" ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
	    
	    sql_cnt_sb.append("select ").append(" count(*) cnt ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
	    
	    if(!StringUtils.isEmpty(where_sb.toString())){
	    	if(where_sb.toString().trim().toLowerCase().startsWith("order by"))
	    	{
	    		sql_sb.append(" ").append(where_sb.toString());
	    		//sql_cnt_sb.append(" ").append(where_sb.toString()); //统计数量不需要排序
	    		
	    		
	    	 
	    	}else{
	    		sql_sb.append("and ").append(where_sb.toString());
	    		
	    		String cnt_where =where_sb.toString();
	    	
	    		//需要去掉order by
	    		int order_idx=cnt_where.toLowerCase().indexOf("order");
	    		if(order_idx!=-1)
	    		{
		    		cnt_where= cnt_where.substring(0, order_idx);
		    		
		    		sql_cnt_sb.append("and ").append(cnt_where);
	    		}
	    		else{
	    			sql_cnt_sb.append("and ").append(cnt_where);
	    		}
	    	}
	    }
	    
	    
	    //处理前台传过来的排序
	    if(condition!=null && condition.getOrderBy()!=null)
	    {
		    Map<String,String> sortMap =condition.getOrderBy();
			if(sortMap.size()>0)
			{
				int pos = sql_sb.toString().trim().indexOf("order by");
					//如果有order by 应该去除之前的排序
				if(pos!=-1){
					
					
					String newWhereSql = sql_sb.substring(0, pos);
					sql_sb.delete(0, sql_sb.length());
					sql_sb.append(newWhereSql);
				}
					//
				Set<Entry<String, String>> sets = sortMap.entrySet();
				
				Iterator<Entry<String, String>>  ite = sets.iterator();
				
				sql_sb.append(" ").append(" order by ");
				StringBuffer sb_order =new StringBuffer();
				
				while(ite.hasNext())
				{
					Entry<String, String> entry = ite.next();
					String k=entry.getKey();
					String v =entry.getValue();
					sb_order.append(" ,");
					sb_order.append(k).append(" ").append(v);
				}
				sb_order.delete(0, 2);
				
				sql_sb.append(sb_order);
			}
	    }
		
		
		
	    
	    
	  //mysql 
	   // sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
	    sql_sb =new StringBuffer(wrapperPagerSql(sql_sb.toString(), condition.getPageIndex(), condition.getPageSize()));
	    
	    final Object[] params = condition.getWherepParameterValues();
	   
	    logger.info("== 查询数据SQL："+sql_sb.toString());
	    
	    
	    List<Map<String, Object>>  dataList=getJdbcTemplate().query(sql_sb.toString(),params,new RowMapper<Map<String,Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int index)
					throws SQLException {
				
				Map<String,Object> item = new HashMap<String, Object>();
				
				
				for(int i=0;i<showDataConfigs.size();i++)
				{
					ShowDataConfig itemDC=showDataConfigs.get(i);
					if(StringUtils.isEmpty(itemDC.getParentTableName()))
					{
						//如果父表 没有填写，那么直接把值放入
						item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldNameAlias()));
					}
					else
					{
						
						//加载 父表对应数据 ，这里只显示一级数据值
						
						ShowDataConfig dataConfig=ArchCache.getInstance().getShowTableConfigCache().getShowDataConfig(itemDC.getParentTableName(),itemDC.getParentShowField());
						
						
						
						String sql_parent = "select "+dataConfig.getFieldName() +" from "+itemDC.getParentTableName() + " where  "+itemDC.getParentTableKey()+"=?";
						logger.info("===sql_parent:"+sql_parent);
						
						Object curr_value = rs.getObject(itemDC.getFieldNameAlias());
						Object parent_value="";
						if(curr_value!=null)
						   parent_value = getJdbcTemplate().queryForObject(sql_parent, new Object[]{rs.getObject(itemDC.getFieldNameAlias())}, Object.class);
						
						item.put(itemDC.getFieldNameAlias(),parent_value);
						
						
					}
					
					
					 //处理 列数字统计
					   //statistics
					
					if(!StringUtils.isEmpty(itemDC.getStatistics()))
					{
						
						StringBuffer statistics_sb = new StringBuffer();
						
						//包含where条件
						statistics_sb.append(itemDC.getStatistics());
						
						String staticsString=getJdbcTemplate().queryForObject(statistics_sb.toString(), params, String.class);
						
						qr.getStatistics().put(itemDC.getFieldNameAlias(), staticsString);
					}
				}
				
				return item;
			}
	    	
	    	
	    });
	    
	    
	    logger.info("== 查询记录数SQL："+sql_cnt_sb.toString());
	    
		   long cnt =  queryForLong(sql_cnt_sb.toString(), params);
		    
		    
		    logger.info("== 总记录数:"+cnt);
	    
	   
	    
	    qr.setResultList(dataList);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(cnt);
		qr.setFields(showDataConfigs);
		
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
