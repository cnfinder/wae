package cn.finder.wae.business.module.auth.dao;

import cn.finder.wae.business.domain.Log;

/********
 * 登陆信息Dao
 * @author Xuchao
 *
 */
public interface LogDao {

	public void add(Log log);
	
	public void update(Log log);
	
	public Log getLastLog(long userId) ;
}
