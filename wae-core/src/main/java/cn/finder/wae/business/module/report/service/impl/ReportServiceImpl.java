package cn.finder.wae.business.module.report.service.impl;

import java.util.List;
import java.util.Map;

import cn.finder.wae.business.domain.report.TReport;
import cn.finder.wae.business.module.report.dao.ReportDao;
import cn.finder.wae.business.module.report.service.ReportService;

public class ReportServiceImpl implements ReportService{
	private ReportDao dao;
	
	public void setDao(ReportDao dao) {
		this.dao = dao;
	}

	
	@Override
	public TReport queryReport(int id) {
		return dao.queryReport(id);
	}

	@Override
	public int updateReport(TReport report) {
		
		return dao.updateReport(report);
	}


	@Override
	public TReport queryReportByShowTableConfigId(int showTableConfigId) {
		
		return dao.queryReportByShowTableConfigId(showTableConfigId);
	}


	@Override
	public List<TReport> queryReportByCondition(Map<String, Object> conditions) {
		return dao.queryReportByCondition(conditions);
	}

}
