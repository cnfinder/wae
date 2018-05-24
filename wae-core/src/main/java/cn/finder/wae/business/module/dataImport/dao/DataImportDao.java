package cn.finder.wae.business.module.dataImport.dao;

import java.util.List;

import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.common.thread.DataImportThread;

public interface DataImportDao {
	public DataImportConfig queryDataImport(int id);

	public int executeDataImport(DataImportConfig dataImport, TableQueryResult queryResult);
	
	public  List<DataImportConfig> findThreads();
	//设置运行状态
	public int setRunningStateYes(Long id);
	
	public DataImportThread startNewThread(DataImportConfig dataImportConfig);
}
