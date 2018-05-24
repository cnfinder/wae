package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;

/***
 * 主要直接提供别名访问 alian  主要用于WHERE 
 * @author wuhualong
 *
 */
public class ReportQueryerExt extends BaseCommonDBQueryer {

	private final static Logger logger = Logger.getLogger(ReportQueryerExt.class);

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
			fieldSql.append(" max(case field_code when ? then value else NULL end) as ? ,");
			para.add(dataConfig.getFieldName());
			para.add(dataConfig.getFieldNameAlias());
		}
		String fieldSqlStr = fieldSql.substring(0, fieldSql.lastIndexOf(","));

		List<Object> para_alians = new ArrayList<Object>();
		// 根据列配置拼接行列转换语句
		for (ShowDataConfig dataConfig : showDataConfigs) {
			para_alians.add(dataConfig.getFieldNameAlias());
		}

		
		
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
		/*String sql = " select " + fieldSqlStr + " from v_report_info " 
				+ " group by row_id, report_id  having " + whereSql;*/
		 String sql =" select report_id,row_id," + fieldSqlStr + " from v_report_info " + " group by row_id, report_id";
					
		 StringBuffer sql_sb=new StringBuffer();
		
		 if(!StringUtils.isEmpty(whereSql.toString())){
			 sql_sb.append(" select ").append(StringUtils.join(para_alians, ",")).append(" from (").append(sql).append(") alian ").append(" where ").append(whereSql.toString());
			 sql=sql_sb.toString();
		 }
		 
		 logger.debug("====查询SQL："+sql);
		 
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList = getJdbcTemplate().queryForList(sql, para.toArray());

		TableQueryResult qr = new TableQueryResult();

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
