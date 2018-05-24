package cn.finder.wae.business.module.common.dao.impl;

import cn.finder.wae.business.domain.OperationLog;
import cn.finder.wae.business.module.common.dao.OperationLogDao;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class OperationLogDaoImpl extends BaseJdbcDaoSupport implements OperationLogDao {

	@Override
	public int addOperationLog(OperationLog operationLog) {
		String sql ="{call proc_operation_log_insert(?,?,?,?,?,?,?,?,?,?,?)}";
		
		return getJdbcTemplate().update(sql, new Object[]{
				operationLog.getUserId(),operationLog.getUserName(),
				operationLog.getLogLevel(),operationLog.getProcessClass(),
				operationLog.getMethodSignature(),operationLog.getArgumentsValue(),
				operationLog.getRefId(),operationLog.getOperationName(),
				operationLog.getOperationDate(),operationLog.getCreateDate(),operationLog.getUpdateDate()
				
		});
	}

}
