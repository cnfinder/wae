package cn.finder.wae.business.module.auth.service;

import cn.finder.wae.business.domain.Log;

public interface LogService {

	 void add(Log log);
	 
	 public Log getLastLog(long userId);
}
