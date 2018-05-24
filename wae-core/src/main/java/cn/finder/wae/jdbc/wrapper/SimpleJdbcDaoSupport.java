package cn.finder.wae.jdbc.wrapper;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class SimpleJdbcDaoSupport extends JdbcDaoSupport {

	
	
	public  int queryForInt(String sql, Object... args){
		
		int retVal=getJdbcTemplate().queryForObject(sql,args, Integer.class);
		return retVal;
	}
	
	public  long queryForLong(String sql, Object... args){
		
		long retVal=getJdbcTemplate().queryForObject(sql,args, Long.class);
		return retVal;
	}
}
