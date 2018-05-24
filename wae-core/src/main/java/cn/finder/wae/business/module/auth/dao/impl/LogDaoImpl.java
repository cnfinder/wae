package cn.finder.wae.business.module.auth.dao.impl;

import cn.finder.wae.business.domain.Log;
import cn.finder.wae.business.module.auth.dao.LogDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class LogDaoImpl extends BaseJdbcDaoSupport implements LogDao {

	@Override
	public void add(Log log) {
		String sql = "insert into t_log (logdate,ip,user_name,logtype,session_id, user_id) values (?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, log.getLogDate(),log.getiP(),log.getUserName(),log.getLogtype(),log.getSessionId(), log.getUser_id());
	}

	@Override
	public void update(Log log) {
		String sql = "insert into t_log (exit_time) values (?) where session_id=";
		
	}

	@Override
	public Log getLastLog(long userId) {
		String sql = "select * from t_log where user_id = ? order by logdate desc";
		Object[] obj = {userId};
		Log log= (Log)queryForSingle(sql, obj, new RowMapperFactory.LogRowMapper());
		return log;
	}


	

}
