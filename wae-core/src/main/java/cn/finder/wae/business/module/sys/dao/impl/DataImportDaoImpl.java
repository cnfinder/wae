package cn.finder.wae.business.module.sys.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.module.common.service.CommOperationService;
import cn.finder.wae.business.module.sys.dao.DataImportDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.thread.DataImportThread;

public class DataImportDaoImpl  extends BaseJdbcDaoSupport implements DataImportDao{
	private CommOperationService commOperationService;
	
	public void setCommOperationService(CommOperationService commOperationService){
		this.commOperationService = commOperationService;
	}
	
	@Override
	public void findEntityById(Long id) {
		
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

	
}
