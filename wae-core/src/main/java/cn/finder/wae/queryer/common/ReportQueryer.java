package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;

public class ReportQueryer extends BaseCommonDBQueryer {

	private final static Logger logger = Logger.getLogger(ReportQueryer.class);

	/*
	 * 显示统计列表页面 1.根据表配置的查找到列配置 2.根据列配置拼接行列转换sql 3.根据sql查询结果，返回
	 */

	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		logger.debug("=====go into CommonQueryer======");

		logger.debug("==showTableConfigId:" + showTableConfigId);
		// 从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		if (condition.getPageSize() == 0) {
			// 默认为数据库设置大小
			condition.setPageSize(showTableConfig.getPageSize() == 0 ? 10
					: showTableConfig.getPageSize());
		}
		if (condition != null) {
			logger.debug("==Sql:" + condition.getSql());
			logger.debug("==WhereCluster:" + condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"
					+ condition.getWherepParameterValues());
			logger.debug("==pageIndex:" + condition.getPageIndex());
			logger.debug("==pageSize:" + condition.getPageSize());
		}

		List<ShowDataConfig> showDataConfigs = ArchCache.getInstance()
				.getShowTableConfigCache()
				.getSearchShowDataConfigs(showTableConfigId);
		StringBuffer fieldSql = new StringBuffer("");
		List<Object> para = new ArrayList<Object>();
		// 根据列配置拼接行列转换语句
		for (ShowDataConfig dataConfig : showDataConfigs) {
			fieldSql.append(" max(case field_code when '"+dataConfig.getFieldName()+"' then value else NULL end) as '"+dataConfig.getFieldNameAlias()+"' ,");
			//fieldSql.append(" max(case field_code when '?' then value else NULL end) as '?' ,");
			//para.add(dataConfig.getFieldName());
			//para.add(dataConfig.getFieldNameAlias());
		}
		String fieldSqlStr = fieldSql.substring(0, fieldSql.lastIndexOf(","));
		
		//处理页面查询条件 
		StringBuffer whereSql = new StringBuffer(" 1=1 ");
		if (condition != null && condition.getWhereCluster() != null
				&& !condition.getWhereCluster().equals("")) {
			whereSql.append(" and " + condition.getWhereCluster());
		}
		if (condition != null && condition.getWherepParameterValues() != null
				&& condition.getWherepParameterValues().length > 0) {
			for (int i = 0; i < condition.getWherepParameterValues().length; i++) {
				para.add(condition.getWherepParameterValues()[i]);
			}
		}
		// 处理showtableconfig 查询条件 sql_config
		StringBuffer showTableConfigwhereSql = new StringBuffer(" 1=1 ");
		if(!StringUtils.isEmpty(showTableConfig.getSqlConfig())){
    		showTableConfigwhereSql.append(" AND "+showTableConfig.getSqlConfig());
    	}
		// 执行查询语句
		StringBuffer sql_sb = new StringBuffer("select * from (  select " + fieldSqlStr + " from v_report_info " 
				+ " group by row_id, report_id  having " +showTableConfigwhereSql +") Table_Abc_Cba where "+ whereSql);
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
				Set<Entry<String, String>> sets = sortMap.entrySet();
				Iterator<Entry<String, String>>  ite = sets.iterator();
				
				sql_sb.append(" ").append(" order by ");
				List<String> orderValue=new ArrayList<String>();
				while(ite.hasNext())
				{
					Entry<String, String> entry = ite.next();
					String k=entry.getKey();
					String v =entry.getValue();
					orderValue.add(k +" " +v);
				}
				sql_sb.append(StringUtils.join(orderValue, ","));
			}
	    }

/*
 * 
 * 

		// 处理查询条件
		StringBuffer whereSql = new StringBuffer(" 1=1 ");
		if (condition != null && condition.getWhereCluster() != null
				&& !condition.getWhereCluster().equals("")) {
			whereSql.append(" and " + condition.getWhereCluster());
		}
		//
		if (condition != null && condition.getWherepParameterValues() != null
				&& condition.getWherepParameterValues().length > 0) {
			for (int i = 0; i < condition.getWherepParameterValues().length; i++) {
				para.add(condition.getWherepParameterValues()[i]);
			}
		}
		
		 if(StringUtils.isEmpty(condition.getWhereCluster()))
		    {
			 	whereSql.append(" AND "+showTableConfig.getSqlConfig());
		    }
		    else{
		    	if(!StringUtils.isEmpty(showTableConfig.getSqlConfig())){
		    		
		    		if(showTableConfig.getSqlConfig().toLowerCase().startsWith("order")){
		    			whereSql.append("  "+showTableConfig.getSqlConfig());
		    		}
		    		else
		    			whereSql.append(" AND "+showTableConfig.getSqlConfig());
		    	}
		    		
		    }
		 
		
		// 执行查询
		StringBuffer sql_sb = new StringBuffer(" select " + fieldSqlStr + " from v_report_info " 
				+ " group by row_id, report_id  having " + whereSql);
		
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
	    */ 
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList = getJdbcTemplate().queryForList(sql_sb.toString(), para.toArray());

		TableQueryResult qr = new TableQueryResult();
		for(ShowDataConfig ds:showDataConfigs){
			if(ds.getDataType()==ConstantsCache.DataType.DATATYPE_NUMBER){
				Iterator<Map<String,Object>> it=dataList.iterator();
				while(it.hasNext()){
					Map<String, Object> map=it.next();
					Object ob=map.get(ds.getFieldNameAlias())==null?"0":map.get(ds.getFieldNameAlias());
					map.put(ds.getFieldNameAlias(), Integer.parseInt(ob.toString()));
				}
			}
		}
		qr.setResultList(dataList);
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(10);
		qr.setCount(Long.valueOf(dataList.size()));
		qr.setShowTableConfig(showTableConfig);
		return qr;
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);

	}
}
