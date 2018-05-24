package cn.finder.wae.business.module.dataImport.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.module.common.service.CommOperationService;
import cn.finder.wae.business.module.dataImport.dao.DataImportDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;
import cn.finder.wae.common.thread.DataImportThread;

public class DataImportDaoImpl extends BaseJdbcDaoSupport implements
		DataImportDao {
	private CommOperationService commOperationService;
	

	public void setCommOperationService(CommOperationService commOperationService) {
		this.commOperationService = commOperationService;
	}

	@Override
	public DataImportConfig queryDataImport(int id) {
		String sql = "select * from t_data_import where id = ?";
		Object[] args = { id };
		DataImportConfig dic = (DataImportConfig) queryForSingle(sql, args,
				new RowMapperFactory.DataImportConfigRowMapper());

		return dic;
	}

	@Override
	public List<DataImportConfig> findThreads() {
		String sql = " select * from t_data_import where frequency > 0 ";
		List<DataImportConfig> list = getJdbcTemplate().query(sql,new  RowMapperFactory.DataImportConfigRowMapper());
		return list;
	}

	@Override
	public int setRunningStateYes(Long id) {
		String sql = "update t_data_import set is_running = 1 where id = " + id;
		return getJdbcTemplate().update(sql);
	}

	@Override
	public DataImportThread startNewThread(DataImportConfig dataImportConfig) {
		DataImportThread dit = new DataImportThread();
		
		dit.setDataImportConfig(dataImportConfig);
		dit.setCommOperationService(commOperationService);
		
		Date newDate = new Date();
		//设置开始查询时间
		if(dataImportConfig.getStartTime() <= newDate.getHours()){  
			newDate.setDate(newDate.getDate()+1); //如果当前时间大于设定时间则日期+1
		}
		else{
			newDate.setHours(dataImportConfig.getStartTime());
		}
		
		Timer timer = new Timer();
		timer.schedule(dit, newDate, dataImportConfig.getFrequency());
		
		return dit;
	}
	
	@Override
	public int executeDataImport(DataImportConfig dic,
			TableQueryResult queryResult) {

		long sourceStcId = dic.getSourceShowTableConfigId();
		long destStcId = dic.getDestShowTableConfigId();
		long processStcId = dic.getProcessShowTableConfigId();

		ShowTableConfig sStc = ArchCache.getInstance()
				.getShowTableConfigCache().get(sourceStcId); // 源数据库表配置
		List<ShowDataConfig> sSdcs = sStc.getShowDataConfigs();

		ShowTableConfig dStc = ArchCache.getInstance()
				.getShowTableConfigCache().get(destStcId);// 目标数据库表配置
		// List<ShowDataConfig> dSdcs = dStc.getShowDataConfigs();

		ShowTableConfig pStc = ArchCache.getInstance()
				.getShowTableConfigCache().get(processStcId); // 匹配
		List<ShowDataConfig> pSdcs = pStc.getShowDataConfigs();

		// 拼接sql
		String sourceTableName = sStc.getShowTableName(); // 源数据库表名
		List<Map<String, Object>> sResult = queryResult.getResultList();
		try {
			if (sResult != null && sResult.size() >= 0) {
				// 导入目标数据库
				if (dic.getIsImport() > 0) {
					for (Map<String, Object> map : sResult) {
						String destTableName = dStc.getShowTableName(); // 目标数据库表名
						Map<String, Object> valueMap = new HashMap<String, Object>();
						for (ShowDataConfig pdc : pSdcs) {
							String sFieldName = pdc.getFieldName();
							String dFieldName = pdc.getFieldNameAlias();
							Object value = map.get(sFieldName);
							valueMap.put(dFieldName, value);
						}
						commOperationService.singleAddRecord(destStcId, destTableName, valueMap); // 单行插入
					}
				}
				// 对源数据备份
				if (dic.getIsBackUp() > 0) {
					HSSFWorkbook workbook = new HSSFWorkbook(); // 定义工作簿
					HSSFSheet sheet = workbook.createSheet(); // 页
					workbook.setSheetName(0, sourceTableName);
					short rowNumber = 0; // 起始行
					HSSFRow row = sheet.createRow(rowNumber);
					rowNumber++;
					HSSFCell cell;
					int nColumn = sSdcs.size();
					for (int i = 0; i < nColumn; i++) // 初始化头
					{
						cell = row.createCell(i, HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(sSdcs.get(i).getFieldName());
					}
					for (Map<String, Object> map : sResult) { // 逐行
						row = sheet.createRow(rowNumber);
						rowNumber++;
						for (int i = 0; i < nColumn; i++) {
							ShowDataConfig sdc = sSdcs.get(i);
							String sFieldName = sdc.getFieldName();
							Object value = map.get(sFieldName);
							cell = row.createCell(i, HSSFCell.CELL_TYPE_STRING);
							// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							if (value != null) {
								cell.setCellValue(value.toString());
							} else {
								cell.setCellValue("");
							}

						}

					}

					// 获取备份目录
					String backUpPath = ArchCache.getInstance()
							.getSysConfigCache().get(SysConfigCache.BackUpPath)
							.getValue();
					// 生成文件名
					SimpleDateFormat time = new SimpleDateFormat(
							"yyyy-MM-dd_HH_mm_ss");
					String fileName = time.format(new Date()) + ".xls";

					// 备份目录+数据源id+表名称+文件名
					String filePath = backUpPath + "\\" + sStc.getTargetDs()
							+ "\\" + sourceTableName + "\\" + fileName;
					// 获取文件目录
					File parent = new File(filePath).getParentFile();
					// 判断目录是否存在，如果不存在就创建目录
					if (parent != null && !parent.exists()) {
						parent.mkdirs();
					}
					FileOutputStream fOut = new FileOutputStream(filePath);
					workbook.write(fOut);
					fOut.flush();
					fOut.close();
				}

				// 对于源数据表删除
				if (dic.getIsDelete() > 0) {
					setDataSourceByShowTableConfigId(sStc.getId());
					String deleteSql = "delete from " + sourceTableName;
					getJdbcTemplate().update(deleteSql);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setDataSourceByShowTableConfigId(Long showTableConfigId) {
		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		DataSource ds = DSManager.getDataSourceMaps().get(
				showTableConfig.getTargetDs());
		setJdbcDataSource(ds);// set datasource
	}
}
