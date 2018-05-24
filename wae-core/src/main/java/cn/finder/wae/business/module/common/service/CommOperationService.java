package cn.finder.wae.business.module.common.service;

import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

public interface CommOperationService {

	
	long[] deleteRows(long showtableConfigId,String[]  ids);
	
	
	@Deprecated
	public int addRecord(String jsondata);
	@Deprecated
	public int editRecord(String jsondata);
	
	public int checkData(String jsonData);  //后台数据验证
	
	//多表添加编辑
	@Deprecated
	public int addRecordCascade(String jsondata);
	@Deprecated
	public int editRecordCascade(String jsondata);
	
	public int singleAddRecord(long showtableConfigId,String tableName, Map<String, Object> data);
	/****
	 * 简单自动补全
	 * @param tableName
	 * @param fieldName
	 * @param key
	 * @return
	 */
	
	//@Deprecated
	//public List<Map<String,Object>> commAutoComplete(long showtableConfigId,String tableName,String fieldName,String key,QueryCondition<Object[]> queryCondition);
	
	
	public List<Map<String,Object>> commAutoComplete(long showtableConfigId,String tableName,String fieldName,QueryCondition<Object[]> queryCondition);
	
	
	public List<Map<String,Object>> commonMutilFieldAutoComplete(long showtableConfigId,QueryCondition<Object[]> queryCondition);
	
	
	byte[] loadBinaryData(long showTableConfigId,String tableName,String primaryKeyField,String primaryKeyValue,String returnField);
	
	
	/*
	 * 
	 * 此处添加和 QueryService同样的接口是为了 把单独功能操作 和通用业务区分开来
	 */
	public TableQueryResult queryTableQueryResult(long showTableConfigId,QueryCondition<Object[]> condition);
	
	
	//不同数据库之间的数据导入
	public boolean dataImport(int dataImportConfigId, QueryCondition<Object[]> condition);
	
	
	/***
	 * 
	 * @author: wuhualong
	 * @data:2014-7-21上午11:27:10
	 * @function: 保存报表设计文件
	 * @param rptId
	 * @param rptData
	 */
	public int updateRptData(long showtableConfigId,int rptId, byte[] rptData);
	
}
