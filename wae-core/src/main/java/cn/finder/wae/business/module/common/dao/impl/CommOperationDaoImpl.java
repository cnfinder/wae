package cn.finder.wae.business.module.common.dao.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hsqldb.lib.StringUtil;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.module.common.dao.CommOperationDao;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.business.module.common.service.impl.QueryServiceImpl;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.db.ds.DSManager;

public class CommOperationDaoImpl extends BaseJdbcDaoSupport implements
		CommOperationDao {

	@Deprecated
	@Override
	public long[] deleteRows(long showtableConfigId, String tableName,
			final List<Long> ids) {

		String sql = "delete from " + tableName + " where id=?";

		final int size = ids.size();

		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, ids.get(i));
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return size;
			}
		};

		getJdbcTemplate().batchUpdate(sql, batchSetter);
		return new long[] {};
	}

	@Override
	public int addRecord(long showTableConfigId, String sql, List list) {
		setDataSourceByShowTableConfigId(showTableConfigId);
		Object[] parameters = list.toArray();
		return getJdbcTemplate().update(sql, parameters);
	}

	@Override
	public int editRecord(long showtableConfigId, String sql, List list) {
		// TODO Auto-generated method stub
		setDataSourceByShowTableConfigId(showtableConfigId);
		Object[] parameters = list.toArray();
		return getJdbcTemplate().update(sql, parameters);
	}

	@Override
	public List<Map<String, Object>> commAutoComplete(long showtableConfigId,
			String tableName, final String fieldName,
			QueryCondition<Object[]> queryCondition) {
		// 判断数据库类型

		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showtableConfigId);

		String sqlConfig = dealWithOrderbySql(showTableConfig.getSqlConfig());

		StringBuffer sql_sb = new StringBuffer();

		if (dbType == DBType.MySql) {
			sql_sb.append("select distinct ").append(fieldName)
					.append(" from ").append(tableName).append(" where ")
					.append(fieldName).append(" like ? ");// .append(" and ").append(sqlConfig).append(" limit 0,20");

			if (!StringUtils.isEmpty(sqlConfig)) {
				sql_sb.append(" AND " + sqlConfig);
			}

			sql_sb.append(" limit 0,20");

		} else if (dbType == DBType.SqlServer) {
			sql_sb.append("select distinct top 20 ").append(fieldName)
					.append(" from ").append(tableName).append(" where ")
					.append(fieldName).append(" like ? ");
			if (!StringUtils.isEmpty(sqlConfig)) {
				sql_sb.append(" AND " + sqlConfig);
			}
		} else if (dbType == DBType.Oracle) {

			StringBuffer sql_tmp = new StringBuffer();

			sql_tmp.append("select ").append(fieldName).append(" from ")
					.append(tableName).append(" where ").append(fieldName)
					.append(" like ?");

			if (!StringUtils.isEmpty(sqlConfig)) {
				sql_tmp.append(" AND " + sqlConfig);
			}
			sql_sb.append("select ").append(fieldName + " " + fieldName)
					.append(" from ( select t.*,rownum rn from (")
					.append(sql_tmp.toString()).append(") t ")
					.append(" where rownum <=").append(20).append(")")
					.append(" where rn >").append(0);
		}

		logger.debug("===commAutoComplete sql:" + sql_sb.toString());
		// return getJdbcTemplate().queryForList(sql_sb.toString(), new
		// Object[]{"%"+key+"%"});
		/*
		 * Object[] para=null; if(queryCondition!=null &&
		 * queryCondition.getWherepParameterValues()!=null &&
		 * queryCondition.getWherepParameterValues().length>0){ int size =
		 * queryCondition.getWherepParameterValues().length+1; para = new
		 * Object[size];
		 * 
		 * para[0] = "%"+key.trim()+"%";
		 * 
		 * for(int i = 1;i<size;i++){ para[i] =
		 * queryCondition.getWherepParameterValues()[i-1]; } }else{ para = new
		 * Object[1];
		 * 
		 * para[0] = "%"+key.trim()+"%"; }
		 */

		// return getJdbcTemplate().query(sql_sb.toString(), new
		// Object[]{"%"+StringUtils.trimToEmpty(key)+"%"},new
		// RowMapper<Map<String, Object>>() {
		return getJdbcTemplate().query(sql_sb.toString(),
				queryCondition.getWherepParameterValues(),
				new RowMapper<Map<String, Object>>() {
					@Override
					public Map<String, Object> mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						// TODO Auto-generated method stub
						Map<String, Object> item = new HashMap<String, Object>();
						item.put(fieldName, rs.getObject(fieldName));
						return item;
					}
				});

	}

	/****
	 * 去掉order by 子句
	 * 
	 * @param sql
	 * @return
	 */
	private String dealWithOrderbySql(String sql) {

		String regexString = "order by[\\s|\\S]+$";

		if (!StringUtils.isEmpty(sql)) {
			String tmp = "";
			tmp = sql.toLowerCase().replaceAll(regexString, "");
			return tmp;
		}

		return sql;
	}

	@Override
	public List<Map<String, Object>> commonMutilFieldAutoComplete(
			long showtableConfigId, String tableName,
			QueryCondition<Object[]> queryCondition) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> tmpData = new ArrayList<Map<String, Object>>();
		for (String fieldName : (String[]) queryCondition.getSearchObject()) {
			List<Map<String, Object>> tdata = commAutoComplete(
					showtableConfigId, tableName, fieldName, queryCondition);
			if (tdata != null && tdata.size() > 0)
				tmpData.addAll(tdata);
		}

		if (tmpData.size() > 0) {

			for (Map<String, Object> item : tmpData) {
				Map<String, Object> itemMap = new HashMap<String, Object>();

				Set<Entry<String, Object>> s = item.entrySet();

				Iterator<Entry<String, Object>> ite = s.iterator();

				while (ite.hasNext()) {

					Entry<String, Object> entry = ite.next();
					itemMap.put("text", entry.getValue());
				}
				data.add(itemMap);
			}
		}

		return data;
	}

	@Override
	public byte[] loadBinaryData(long showTableConfigId, String tableName,
			String primaryKeyField, String primaryKeyValue,
			final String returnField) {
		String sql = "select " + returnField + " from " + tableName + " where "
				+ primaryKeyField + " =?";
		byte[] data = null;
		data = queryForSingle(sql, new Object[] { primaryKeyValue },
				new RowMapper<byte[]>() {

					@Override
					public byte[] mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						// TODO Auto-generated method stub
						InputStream in = rs.getBinaryStream(returnField);
						byte[] buf = null;
						if (in != null) {
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							int b = 0;
							try {
								while ((b = in.read()) != -1)
									baos.write(b);
								buf = baos.toByteArray();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						return buf;
					}
				});
		return data;
	}

	/**
	 * 多表添加 1.将前台传入的json字符串转换成MAP对象 2.查看是否showtableconfigid是否为空
	 * 3.根据showtableconfigid从缓存中获取对应信息 4.获取fieldTableName数组tables
	 * 5.遍历tables，获取对应列值（前台对应列的名称前=fieldTableName.fieldName)，调用单个表添加方法
	 * 
	 * 返回
	 * 
	 */
	@Override
	public int addRecordCascade(long showTableConfigId, Map<String, Object> data) {

		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		List<ShowDataConfig> showDataConfigs = showTableConfig
				.getShowDataConfigs();

		Map<String, String> tables = new HashMap<String, String>();
		for (ShowDataConfig sdc : showDataConfigs) {
			String tableName = sdc.getFieldTableName();
			if (!StringUtil.isEmpty(tableName)) {
				if (!tables.containsKey(tableName)) { //
					String jsonStr = String.valueOf(data.get(tableName));
					tables.put(tableName, jsonStr);
				}

			}
		}

		Set<String> key = tables.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String tableName = it.next();
			Map<String, Object> map = JsonUtil.getMap4Json(tables
					.get(tableName));
			singleAddRecord(showTableConfigId, tableName, map);
		}

		return 0;
	}

	@Override
	public int singleAddRecord(long showTableConfigId, String talbeName,
			Map<String, Object> data) {
		setDataSourceByShowTableConfigId(showTableConfigId);
		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		String sql = "insert into " + talbeName + "(";
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		List<ShowDataConfig> showDataConfigs = showTableConfig
				.getShowDataConfigs();

		for (ShowDataConfig showDataConfig : showDataConfigs) {
			if (!showDataConfig.getFieldTableName().equals(talbeName)) { // 排除不是表tableName的字段
				continue;
			}
			String fieldName = showDataConfig.getFieldName();
			String[] strs = fieldName.split("\\."); // 删除表名前缀
			fieldName = strs[strs.length - 1];
			Object value = data.get(fieldName);
			// 系统日期类型控件，赋于系统当前日期
			if (showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE) {
				sql += fieldName + ", ";
				valueSql += " ?, ";
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			if (value != null
					&& !value.equals("")
					&& showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM) { // 33不是需要插入到数据库的类型
				sql += fieldName + ", ";
				valueSql += " ?, ";
				values.add(value);
			}
		}
		sql = sql.substring(0, sql.lastIndexOf(",")); // 删除最后一个","字符
		valueSql = valueSql.substring(0, valueSql.lastIndexOf(","));
		sql += ") values(" + valueSql + ")";
		Object[] parameters = values.toArray();
		return getJdbcTemplate().update(sql, parameters);
	}

	@Override
	public int editRecordCascade(long showTableConfigId,
			Map<String, Object> data) {
		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		List<ShowDataConfig> showDataConfigs = showTableConfig
				.getShowDataConfigs();

		Map<String, Map<String, Object>> tables = new HashMap<String, Map<String, Object>>();
		for (ShowDataConfig sdc : showDataConfigs) {
			String tableName = sdc.getFieldTableName();
			if (!StringUtil.isEmpty(tableName)) {
				if (data.get(tableName) == null) {
					return -3; // 字段表配置错误
				}
				if (!tables.containsKey(tableName)) { // 判断是表是否已经初始化过
					String jsonStr = String.valueOf(data.get(tableName));
					Map<String, Object> table = new HashMap<String, Object>();
					table.put("tableName", tableName);
					table.put("jsondata", jsonStr);
					List<ShowDataConfig> fields = new ArrayList<ShowDataConfig>();
					fields.add(sdc);
					table.put("fields", fields);
					tables.put(tableName, table);
				} else {
					Map<String, Object> table = tables.get(tableName);
					@SuppressWarnings("unchecked")
					List<ShowDataConfig> list = (List<ShowDataConfig>) table
							.get("fields");
					list.add(sdc);
				}

			}
		}

		Set<String> key = tables.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String tableName = it.next();
			Map<String, Object> map = tables.get(tableName);
			singlEditRecord(showTableConfigId, tableName, map);
		}
		return 0;
	}

	public int singlEditRecord(long showTableConfigId, String tableName,
			Map<String, Object> map) {
		String jsondata = String.valueOf(map.get("jsondata"));
		Map<String, Object> data = JsonUtil.getMap4Json(jsondata);

		String primaryKey = "";

		String sql = "";
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		@SuppressWarnings("unchecked")
		List<ShowDataConfig> showDataConfigs = (List<ShowDataConfig>) map
				.get("fields");

		for (ShowDataConfig showDataConfig : showDataConfigs) {
			String fieldName = showDataConfig.getFieldName();
			String[] strs = fieldName.split("\\.");
			fieldName = strs[strs.length - 1];
			Object value = data.get(fieldName);

			if (showDataConfig.getIsPrimaryKey() == 1) { // 判断是否是主键
				primaryKey = fieldName;
				continue;
			}
			if (showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE) {
				valueSql += fieldName + " = ?, ";
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			if (value != null
					&& showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM) { // 33不是需要插入到数据库的类型
				if (value.equals("")) {
					if (showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD) {
						if (showDataConfig.getDataType() != ConstantsCache.DataType.DATATYPE_STRING) {
							if (!StringUtils.isEmpty(showDataConfig
									.getDefaultValue())) {
								value = showDataConfig.getDefaultValue();
							} else {
								if (showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_DATE) {
									value = null;
								}
								if (showDataConfig.getShowType() == ConstantsCache.DataType.DATATYPE_NUMBER) {
									value = -1;
								}
							}

						}
						valueSql += fieldName + " = ?, ";
						values.add(value);

					}

				} else {
					valueSql += fieldName + " = ?, ";
					values.add(value);
				}
			}
		}

		valueSql = valueSql.substring(0, valueSql.lastIndexOf(","));// 删除最后一个","

		sql = "update " + tableName + " set " + valueSql;
		sql += " where 1=1";
		sql += " and " + primaryKey + " = " + data.get(primaryKey);

		if (data.get(primaryKey) == null
				|| StringUtil.isEmpty(String.valueOf(data.get(primaryKey)))) {
			return -4;// 主键设置错误！
		}

		return getJdbcTemplate().update(sql, values.toArray());
	}

	@Override
	public boolean dataImport(DataImportConfig dic, TableQueryResult queryResult) {
		try {

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
			String whereCluster = " where 1=1 ";
			if (sStc.getSqlConfig() != null && !sStc.getSqlConfig().equals("")) {
				whereCluster += " and " + sStc.getSqlConfig();
			}
			String sSqlString = "select * from " + sourceTableName
					+ whereCluster; // 查询语句
			// if(dic.getFrequency() <= 0l){
			 QueryService queryService = new QueryServiceImpl();
//			 TableQueryResult sResult = queryService.queryTableQueryResult(sourceStcId, null);
			List<Map<String, Object>> sResult = queryFromResource(sourceStcId,
					sSqlString);
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
						singleAddRecord(destStcId, destTableName, valueMap); // 单行插入
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
		// }
		return true;
	}

	public List<Map<String, Object>> queryFromResource(long showTableConfigId,
			String sql) {
		setDataSourceByShowTableConfigId(showTableConfigId);
		return getJdbcTemplate().queryForList(sql);
	}

	public DataImportConfig getDataImportById(long id) {
		String sql = "select * from t_data_import where id = ?";
		Object[] args = { id };
		DataImportConfig dic = (DataImportConfig) queryForSingle(sql, args,
				new RowMapperFactory.DataImportConfigRowMapper());

		return dic;

	}

	public void setDataSourceByShowTableConfigId(Long showTableConfigId) {
		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		DataSource ds = DSManager.getDataSourceMaps().get(
				showTableConfig.getTargetDs());
		setJdbcDataSource(ds);// set datasource
	}

	@Override
	public BufferedImage getBufferedImage(long showTableConfigId,
			String primaryKeyValue, String fieldName) {

		ShowTableConfig showTableConfig = ArchCache.getInstance()
				.getShowTableConfigCache().get(showTableConfigId);
		ShowDataConfig showDataConfig = ArchCache.getInstance()
				.getShowTableConfigCache()
				.getPKShowDataConfig(showTableConfigId);
		String sql = "select " + fieldName + " from "
				+ showTableConfig.getShowTableName() + " where "
				+ showDataConfig.getFieldName() + "=?";
		Object[] values = { primaryKeyValue };
		List<BufferedImage> images = getJdbcTemplate().query(sql, values,
				new RowMapper<BufferedImage>() {

					@Override
					public BufferedImage mapRow(ResultSet rs, int arg1)
							throws SQLException {
						BufferedImage image = null;
						try {
							InputStream is =rs.getBinaryStream(1);
							if(is!=null)
								image = ImageIO.read(is);
						} catch (IOException e) {
							logger.error("没有二进制数据信息...");
						}
						return image;
					}

				});
		if (images != null && images.size() > 0) {
			return images.get(0);
		}
		return null;
	}
	
	


	/* (non-Javadoc)
	 * @see cn.finder.wae.business.module.common.dao.CommOperationDao#saveRptData(int, byte[])
	 */
	@Override
	public int updateRptData(long showtableConfigId,int rptId, byte[] rptData) {
		// TODO Auto-generated method stub
		
		String sql ="update t_rpt set rpt_template_file =?,version=version+1 where id =?";
		
		
		int cnt = getJdbcTemplate().update(sql,new Object[]{rptData,rptId});
		return cnt;
		
	}

	

	@Override
	public InputStream getBufferedFile(long showTableConfigId,
			String primaryKeyValue, String fieldName) {
		

			ShowTableConfig showTableConfig = ArchCache.getInstance()
					.getShowTableConfigCache().get(showTableConfigId);
			ShowDataConfig showDataConfig = ArchCache.getInstance()
					.getShowTableConfigCache()
					.getPKShowDataConfig(showTableConfigId);
			String sql = "select " + fieldName + " from "
					+ showTableConfig.getShowTableName() + " where "
					+ showDataConfig.getFieldName() + "=?";
			Object[] values = { primaryKeyValue };
			List<InputStream> images = getJdbcTemplate().query(sql, values,
					new RowMapper<InputStream>() {

						@Override
						public InputStream mapRow(ResultSet rs, int arg1)
								throws SQLException {
							byte is[] = new byte[1024];
							is= rs.getBytes(1);
							InputStream sbs = new ByteArrayInputStream(is); 
							return sbs;
						}

					});
			if (images != null && images.size() > 0) {
				return images.get(0);
			}
			return null;
	}

}
