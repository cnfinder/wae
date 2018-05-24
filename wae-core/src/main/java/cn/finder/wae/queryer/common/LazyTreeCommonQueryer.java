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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hsqldb.lib.StringUtil;
import org.springframework.jdbc.core.RowMapper;


import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;

public class LazyTreeCommonQueryer extends  BaseCommonDBQueryer{

	private final static Logger logger = Logger.getLogger(TreeCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into CommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		final TableQueryResult qr =new TableQueryResult();
		
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		final String tableName = showTableConfig.getShowTableName();
		
		qr.setShowTableConfig(showTableConfig);
		
		if(StringUtil.isEmpty(showTableConfig.getSqlConfig())){
			
		}
		
		
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
		
	    final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
	    
	    StringBuffer sql_sb =new StringBuffer();
	    
	    StringBuffer sql_cnt_sb = new StringBuffer();
	    
	    
	    // "id 编号,name 名称"
	    StringBuffer fields_sb =new StringBuffer();
	    
	    // where 后面的条件
	    StringBuffer where_sb =new StringBuffer();
	    
	    
	   final  List<String> filedNames=new ArrayList<String>();
	   
	    
	    for(ShowDataConfig df:showDataConfigs)
	    {
	    	filedNames.add(df.getFieldName()+" "+df.getFieldNameAlias());
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
	    		if(!StringUtils.isEmpty(showTableConfig.getSqlConfig())){
	    		
	    		if(showTableConfig.getSqlConfig().toLowerCase().startsWith("order")){
	    			where_sb.append("  "+showTableConfig.getSqlConfig());
	    		}
	    		else
	    			where_sb.append(" AND "+showTableConfig.getSqlConfig());
	    	}
	    }
	    
	    sql_sb.append("select ").append(fields_sb+" ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
	    
	    sql_cnt_sb.append("select ").append(" count(*) cnt ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
	    //oracle
	    //sql_sb.append(" and ").append(" (rownum >").append((condition.getPageIndex()-1)*condition.getPageSize()).append(" and ").append(" rownum <").append(condition.getPageSize()+") ");
	    
	    
	  
	    
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
	  //  sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
	    
	    
	    sql_sb =new StringBuffer(wrapperPagerSql(sql_sb.toString(), condition.getPageIndex(), condition.getPageSize()));
	    
	    if(sql_sb.toString().contains("?") && condition.getWherepParameterValues() == null){   //如果传入参数为空，将参数设为0
	    	Object[] p = {0};
	    	condition.setWherepParameterValues(p);
	    }
	    final Object[] params = condition.getWherepParameterValues();
	   
	    logger.debug("== 查询数据SQL："+sql_sb.toString());
	    
	    List<Map<String, Object>>  dataList=getJdbcTemplate().query(sql_sb.toString(),params,new RowMapper<Map<String,Object>>(){

	    	@Override
			public Map<String, Object> mapRow(ResultSet rs, int index)
					throws SQLException {
				
				Map<String,Object> item = new HashMap<String, Object>();
				
				/*
				for(String field:filedNames)
				{
					item.put(field, rs.getObject(field));
				}*/
				
				
				
				for(int i=0;i<showDataConfigs.size();i++)
				{
					ShowDataConfig itemDC=showDataConfigs.get(i);
					if(StringUtils.isEmpty(itemDC.getParentTableName()))
					{
						//如果父表 没有填写，那么直接把值放入
						item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldNameAlias()));
					}
				}
				//做第二层节点数量查询
				final Object[] param = {item.get("id")};
				String childSql = "select count(*) from " + tableName + " where parent_id =? ";
				if(queryForInt(childSql, param) >0){  //，返回结果size>0将item的isleaf,expanded 设为false,
					item.put("isLeaf", false);
					item.put("expanded", false);
					item.put("asyncLoad",false);        //节点属性，取消懒加载，既节点在被加载时不向服务器取数据
					item.put("isLoaded",false);
				}
				
				return item;
			}
	    	
	    	
	    });
	    
	    
	    logger.debug("== 查询记录数SQL："+sql_cnt_sb.toString());
	    
	   long cnt =  queryForLong(sql_cnt_sb.toString(), params);
	    
	    
	    logger.debug("== 总记录数:"+cnt);
	    
	    qr.setResultList(dataList);
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(cnt);
		
		
		qr.setShowTableConfig(showTableConfig);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
}
