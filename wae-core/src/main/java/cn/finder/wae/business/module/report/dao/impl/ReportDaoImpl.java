package cn.finder.wae.business.module.report.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.report.TReport;
import cn.finder.wae.business.module.report.dao.ReportDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;

public class ReportDaoImpl extends BaseJdbcDaoSupport implements ReportDao{

	@Override
	public TReport queryReport(int id) {
		String sql = " select * from t_report where id = ?";
		Object[] para = {
				id
		};
		List<TReport> reports = getJdbcTemplate().query(sql, para, new RowMapperFactory.ReportRowMapper());
		if(reports != null && reports.size() > 0){
			return reports.get(0);
		}
		return null;
	}

	@Override
	public int updateReport(TReport report) {
		setDataSourceByShowTableConfigId(new Long(report.getShowtableConfigId()));
		
		String sql = " update t_report set ";
		StringBuffer changeSql = new StringBuffer("");
		List<Object>  list = new ArrayList<Object>();
		if(report!= null ){
			if(report.getLastUpdate() != null){
				changeSql.append(" last_update = ?");
				list.add(report.getLastUpdate());
			}
			if(report.getId() != null){
				changeSql.append(" where id = ?	");
				list.add(report.getId());
			}else{
				return 0 ;
			}
			
		}
		try{
		return getJdbcTemplate().update(sql + changeSql, list.toArray());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public TReport queryReportByShowTableConfigId(int showTableConfigId) {
		String sql = "select * from t_report where showtable_config_id = ? ";
		Object[] para = {
				showTableConfigId
		};
		List<TReport> reports = getJdbcTemplate().query(sql, para, new RowMapperFactory.ReportRowMapper());
		if(reports != null && reports.size() > 0){
			return reports.get(0);
		}
		return null;
	}

	@Override
	public List<TReport> queryReportByCondition(Map<String, Object> conditions) {
		StringBuffer sql = new StringBuffer("select * from t_report where 1=1 ");
		List<Object> para = new ArrayList<Object>();
		if(conditions != null && conditions.size() > 0){
			Set<String> keys = conditions.keySet();
			for(String key : keys){
				sql.append(" and "+ key + " = ? " );
				para.add(conditions.get(key));
			}
		}
		List<TReport> reports = getJdbcTemplate().query(sql.toString(), para.toArray(), new RowMapperFactory.ReportRowMapper());
		return reports;
	}

	public void setDataSourceByShowTableConfigId(Long showTableConfigId) {
		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		DataSource ds = DSManager.getDataSourceMaps().get(
				showTableConfig.getTargetDs());
		setJdbcDataSource(ds);// set datasource
	}
}
