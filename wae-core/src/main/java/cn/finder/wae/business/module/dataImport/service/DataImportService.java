package cn.finder.wae.business.module.dataImport.service;

import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.TableQueryResult;

public interface DataImportService {
	public DataImportConfig queryDataImport(int id);
	
	public int  executeDataImport(DataImportConfig dataImport, TableQueryResult queryResult);
}
