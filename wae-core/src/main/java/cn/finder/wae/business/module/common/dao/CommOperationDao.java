package cn.finder.wae.business.module.common.dao;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.TableQueryResult;

public interface CommOperationDao{

	

	/**
	 * remove to QueryService interface
	 * @param showtableConfigId
	 * @param tableName
	 * @param ids
	 * @return
	 */
	long[] deleteRows(long showtableConfigId,String tableName,List<Long>  ids);
	public int addRecord(long showTableConfigId,String sql, List<Object> list);
	public int editRecord(long showtableConfigId,String sql,List list);
	public int singleAddRecord(long showtableConfigId,String tableName, Map<String, Object> data);
	public int addRecordCascade(long showtableConfigId, Map<String, Object> data);
	public int editRecordCascade(long showtableConfigId, Map<String, Object> data);
	
	//@Deprecated
	//public List<Map<String,Object>> commAutoComplete(long showtableConfigId,String tableName,String fieldName,String key,QueryCondition<Object[]> queryCondition);
	
	public List<Map<String,Object>> commAutoComplete(long showtableConfigId,String tableName,String fieldName,QueryCondition<Object[]> queryCondition);
	
	
	public List<Map<String,Object>> commonMutilFieldAutoComplete(long showtableConfigId,String tableName,QueryCondition<Object[]> queryCondition);
	
	byte[] loadBinaryData(long showTableConfigId,String tableName,String primaryKeyField,String primaryKeyValue,final String returnField);
	
	public boolean dataImport(DataImportConfig dic, TableQueryResult queryResult);
	
	public BufferedImage getBufferedImage(long showTableConfigId, String primaryKeyValue, String fieldName);
	public InputStream getBufferedFile(long showTableConfigId, String primaryKeyValue, String fieldName);
	public int updateRptData(long showtableConfigId,int rptId,byte[] rptData);
	
}
