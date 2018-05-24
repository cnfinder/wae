package cn.finder.wae.business.module.report.dao;

import java.util.List;
import java.util.Map;

import cn.finder.wae.business.domain.report.TReport;

public interface ReportDao {
	public TReport queryReport(int id);
	public TReport queryReportByShowTableConfigId(int showTableConfigId) ;
	public List<TReport> queryReportByCondition(Map<String, Object> conditions) ;
	public int updateReport(TReport report);
}
