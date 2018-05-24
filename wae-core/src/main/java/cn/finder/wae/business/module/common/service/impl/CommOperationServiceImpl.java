package cn.finder.wae.business.module.common.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hsqldb.lib.StringUtil;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.report.TReport;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.dao.CommOperationDao;
import cn.finder.wae.business.module.common.service.CommOperationService;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.business.module.dataImport.service.DataImportService;
import cn.finder.wae.business.module.report.service.ReportService;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.JsonUtil;

public class CommOperationServiceImpl implements CommOperationService {

	private CommOperationDao commOperationDao;
	
	private QueryService queryService;
	private DataImportService dataImportService;
	private ReportService reportService;
	
	private Logger log = Logger.getLogger(CommOperationServiceImpl.class);
	
	public void setDataImportService(DataImportService dataImportService) {
		this.dataImportService = dataImportService;
	}



	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}



	public void setCommOperationDao(CommOperationDao commOperationDao) {
		this.commOperationDao = commOperationDao;
	}
	
	

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}



	@Override
	public long[] deleteRows(long showtableConfigId, String[] ids) {
		
		DeleteQueryConditionDto deleteDto = new DeleteQueryConditionDto();
		deleteDto.setShowtableConfigId(showtableConfigId);
		deleteDto.setIds(ids);
		queryService.queryTableQueryResult(showtableConfigId, deleteDto);
		return null;
	}

	@Override
	public int addRecord(String jsondata) {
		Map<String, Object> data = JsonUtil.getMap4Json(jsondata);
		
		Long showTableConfigId = Long.valueOf(String.valueOf(data.get("showtableConfigId001abcX")));
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		if(checkData(jsondata) < 0){ //后台验证
			return -1;
		}
		
		String sql = "insert into " + data.get("table001abcX") + "(";
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		for(ShowDataConfig showDataConfig:showDataConfigs){
			String fieldName = showDataConfig.getFieldName();
			Object value = data.get(fieldName);
			//系统日期类型控件，赋于系统当前日期
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE){
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			if(value != null && !value.equals("")&& showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM){ //33不是需要插入到数据库的类型
//				if(value.equals("")){
//					if(showDataConfig.getDataType() != 11){
//						value = showDataConfig.getDefaultValue();
//					}
//				}
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(value);
			}
		}
//		sql += " create_date)" ;//添加创建日期
//		valueSql += "?";
//		values.add(new Timestamp(System.currentTimeMillis()));
		sql = sql.substring(0,sql.lastIndexOf(","));     //删除最后一个","字符
		valueSql = valueSql.substring(0,valueSql.lastIndexOf(","));
		sql += ") values(" + valueSql + ")";
		return commOperationDao.addRecord(showTableConfigId,sql, values);
	}
	

	@Override
	public int editRecord(String jsondata) {
		Map<String, Object> data = JsonUtil.getMap4Json(jsondata);
		
		
		if(checkData(jsondata) < 0){ //后台验证
			return -1;
		}
		
		Long showTableConfigId = Long.valueOf(String.valueOf(data.get("showtableConfigId001abcX")));
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		ShowDataConfig primaryKeyConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		String primaryKey = primaryKeyConfig.getFieldName();
		if(data.get(primaryKey) == null || StringUtil.isEmpty(String.valueOf(data.get(primaryKey)))){
			log.info("主键设置错误！");
			return -1;
		}
		String sql = "" ;
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		for(ShowDataConfig showDataConfig:showDataConfigs){
			String fieldName = showDataConfig.getFieldName();
			Object value = data.get(fieldName);
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE){
				valueSql += fieldName + " = ?, ";
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			if (value != null && showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM) { // 33不是需要插入到数据库的类型
				if (value.equals("")) {
					if (showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD) {
						if (showDataConfig.getDataType() != ConstantsCache.DataType.DATATYPE_STRING) {
							if(!StringUtils.isEmpty(showDataConfig.getDefaultValue())){
								value = showDataConfig.getDefaultValue();
							}else{
								if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_DATE){
									value = null;
								}
								if(showDataConfig.getShowType() == ConstantsCache.DataType.DATATYPE_NUMBER){
									value = -1;
								} 
							}
							
						}
						valueSql += fieldName + " = ?, ";
						values.add(value);
						
					}
					
				}else{
					valueSql += fieldName + " = ?, ";
					values.add(value);
				}
			}
		}
		
		
		valueSql = valueSql.substring(0,valueSql.lastIndexOf(","));//删除最后一个","
		
		sql = "update " + data.get("table001abcX") + " set " +valueSql;
		sql += " where 1=1";
		sql += " and "+ primaryKey + " = " +data.get(primaryKey); 

		return commOperationDao.editRecord(showTableConfigId,sql, values);
	}
	
	
	@Override
	public int checkData(String jsonData) {
		Logger log = Logger.getLogger("");
		
		Map<String, Object> data = JsonUtil.getMap4Json(jsonData);
		log.setLevel(Level.INFO);
		log.info("showtableConfigId:"+ data.get("showtableConfigId001abcX"));

		return 0;
	}

	@Override
	public List<Map<String, Object>> commAutoComplete(long showtableConfigId,String tableName,
			String fieldName,QueryCondition<Object[]> queryCondition) {
		
		return commOperationDao.commAutoComplete(showtableConfigId,tableName, fieldName,queryCondition);
	}

	@Override
	public List<Map<String, Object>> commonMutilFieldAutoComplete(
			long showtableConfigId,QueryCondition<Object[]> queryCondition) {
		// TODO Auto-generated method stub
		ShowTableConfig sdc=ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId);
		return commOperationDao.commonMutilFieldAutoComplete(showtableConfigId,sdc.getShowTableName(),queryCondition);
	}

	@Override
	public byte[] loadBinaryData(long showTableConfigId, String tableName,
			String primaryKeyField, String primaryKeyValue, String returnField) {
		// TODO Auto-generated method stub
		return commOperationDao.loadBinaryData(showTableConfigId, tableName, primaryKeyField, primaryKeyValue, returnField);
	}



	@Override
	public int addRecordCascade(String jsondata) {
		Map<String, Object> data = JsonUtil.getMap4Json(jsondata);
		
		Object showTableConfigIdObj = data.get("showtableConfigId001abcX");
		Long showTableConfigId;
		try{
			showTableConfigId = Long.valueOf(String.valueOf(showTableConfigIdObj));
		}catch(Exception e){
			e.printStackTrace();
			return -1; // 数据表配置错误
		}
		
		return commOperationDao.addRecordCascade(showTableConfigId,data);
	}
	
	@Override
	public int singleAddRecord(long showtableConfigId,String tableName, Map<String, Object> data){
		return commOperationDao.singleAddRecord(showtableConfigId, tableName, data);
	}

	@Override
	public int editRecordCascade(String jsondata) {
		Map<String, Object> data = JsonUtil.getMap4Json(jsondata);
		Object showTableConfigIdObj = data.get("showtableConfigId001abcX");
		Long showTableConfigId;
		try{
			showTableConfigId = Long.valueOf(String.valueOf(showTableConfigIdObj));
		}catch(Exception e){
			e.printStackTrace();
			return -1; // 数据表配置错误
		}
		return commOperationDao.editRecordCascade(showTableConfigId,data);
	}



	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		return queryService.queryTableQueryResult(showTableConfigId, condition);
	}



	@Override
	public boolean dataImport(int dataImportConfigId, QueryCondition<Object[]> condition) {
		DataImportConfig dic = dataImportService.queryDataImport(dataImportConfigId);
		//执行查询，根据源数据表配置查询结果
		condition.setPageSize(0);
		TableQueryResult queryResult = queryService.queryTableQueryResult(dic.getSourceShowTableConfigId(), condition);
		//封装参数
		MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
		para.put("queryResult", queryResult);
		
		TReport report = reportService.queryReport(dic.getReportId());
		
		para.put("report", report);
		//根据DataImportConfig配置执行数据导入、备份、删除
		dataImportService.executeDataImport(dic, queryResult);
		//执行统计操作
		if (dic.getReportId() > 0) {
			queryService.queryTableQueryResult(dic.getDestShowTableConfigId(), para);
			report.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			reportService.updateReport(report);
		}
		return	true;
	}



	/* (non-Javadoc)
	 * @see cn.finder.wae.business.module.common.service.CommOperationService#saveRptData(int, byte[])
	 */
	@Override
	public int updateRptData(long showtableConfigId,int rptId, byte[] rptData) {
		// TODO Auto-generated method stub
		return commOperationDao.updateRptData(showtableConfigId,rptId, rptData);
	}
	
	
	/*
	@Override
	public boolean dataImport(int dataImportConfigId, QueryCondition<Object[]> condition) {
		DataImportConfig dic = dataImportService.queryDataImport(dataImportConfigId);		
		MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
		para.put("dataImportConfig", dic);
		queryService.queryTableQueryResult(dic.getProcessShowTableConfigId(), para);
		return true;
	}
	*/
}
