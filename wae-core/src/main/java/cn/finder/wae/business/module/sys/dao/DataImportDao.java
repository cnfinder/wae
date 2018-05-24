package cn.finder.wae.business.module.sys.dao;

import java.util.List;

import org.springframework.core.task.TaskExecutor;

import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.common.thread.DataImportThread;

public interface DataImportDao {
	
	public void findEntityById(Long id);
	public  List<DataImportConfig> findThreads();
	//设置运行状态
	public int setRunningStateYes(Long id);
	
	public DataImportThread startNewThread(DataImportConfig dataImportConfig);
}
