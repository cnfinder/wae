package cn.finder.wae.business.module.auth.service.impl;

import cn.finder.wae.business.domain.Log;
import cn.finder.wae.business.module.auth.dao.LogDao;
import cn.finder.wae.business.module.auth.service.LogService;

public class LogServiceImpl implements LogService {

	private LogDao logDao;
	
	@Override
	public void add(Log log) {
		logDao.add(log);
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	@Override
	public Log getLastLog(long userId) {
		// TODO Auto-generated method stub
		return this.logDao.getLastLog(userId);
	}

	
	
	
}
