package cn.finder.wae.business.module.common.service.impl;

import cn.finder.wae.business.domain.OperationLog;
import cn.finder.wae.business.module.common.dao.OperationLogDao;
import cn.finder.wae.business.module.common.service.OperationLogService;

public class OperationLogServiceImpl implements OperationLogService {

	private OperationLogDao operationLogDao;
	
	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	@Override
	public int addOperationLog(OperationLog operationLog) {
		return operationLogDao.addOperationLog(operationLog);
	}
	

}
