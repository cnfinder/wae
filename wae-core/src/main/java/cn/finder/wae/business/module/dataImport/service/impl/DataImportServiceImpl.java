package cn.finder.wae.business.module.dataImport.service.impl;


import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.module.dataImport.dao.DataImportDao;
import cn.finder.wae.business.module.dataImport.service.DataImportService;

public class DataImportServiceImpl implements DataImportService{
	private DataImportDao dataImportDao;
	
	
	
	public void setDataImportDao(DataImportDao dataImportDao) {
		this.dataImportDao = dataImportDao;
	}

	@Override
	public DataImportConfig queryDataImport(int id) {
		
		return dataImportDao.queryDataImport(id);
	}

	@Override
	public int executeDataImport(DataImportConfig dataImport, TableQueryResult queryResult) {
		
		return dataImportDao.executeDataImport(dataImport,  queryResult);
	}

}
