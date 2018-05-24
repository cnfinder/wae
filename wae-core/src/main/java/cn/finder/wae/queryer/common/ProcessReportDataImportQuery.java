package cn.finder.wae.queryer.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.domain.report.TReport;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.dao.QueryDao;
import cn.finder.wae.business.module.common.dao.impl.QueryDaoImpl;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.business.module.dataImport.service.DataImportService;
import cn.finder.wae.business.module.dataImport.service.impl.DataImportServiceImpl;
import cn.finder.wae.business.module.report.service.ReportService;
import cn.finder.wae.business.module.report.service.impl.ReportServiceImpl;
import cn.finder.wae.business.module.sys.service.SysService;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.thread.AppContent;


/***
 * @author wanggan
 *  统计数据处理
 *
 */
public class ProcessReportDataImportQuery extends  BaseCommonDBQueryer{

	
private final static Logger logger = Logger.getLogger(ProcessReportDataImportQuery.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){		
		logger.debug("=====go into ProcessReportDataImportQuery======");		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		//DataImportConfig dic=(DataImportConfig)data.get("dataImportConfig");
		//ServletContext servletContext=data.get("servletContext")==null?((HttpSession)data.get("SESSION")).getServletContext():(ServletContext)data.get("servletContext");
		
		QueryService queryService =AppApplicationContextUtil.getContext().getBean("queryService", QueryService.class);
		DataImportService dataImportService = AppApplicationContextUtil.getContext().getBean("dataImportServiceImpl",DataImportService.class);
		ReportService reportService=  AppApplicationContextUtil.getContext().getBean("reportServiceImpl",ReportService.class);
		
		
		//T_DATA_REPORT  统计管理项
		Integer dataImportConfigId=data.get("dataImportConfig")==null?((TaskPlan)data.get("taskplan")).getInt("dataImportConfig"):(Integer)data.get("dataImportConfig");
		
		DataImportConfig dic = dataImportService.queryDataImport(dataImportConfigId);
		
		
		
		condition.setPageSize(ArchCache.getInstance().getShowTableConfigCache().get(dic.getSourceShowTableConfigId()).getPageSize());
		
		//获取 数据来源
		TableQueryResult queryResult = queryService.queryTableQueryResult(dic.getSourceShowTableConfigId(), condition);
		
		TReport report = reportService.queryReport(dic.getReportId());
		
		
		//根据DataImportConfig配置执行数据导入、备份、删除
		dataImportService.executeDataImport(dic, queryResult);
		
		((MapParaQueryConditionDto)condition).put("report", report);
		((MapParaQueryConditionDto)condition).put("queryResult", queryResult);
		//执行统计操作
		if (dic.getReportId() > 0) {
			queryService.queryTableQueryResult(dic.getDestShowTableConfigId(), condition);
			report.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			reportService.updateReport(report);
		}
		return null;
	}
	
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
