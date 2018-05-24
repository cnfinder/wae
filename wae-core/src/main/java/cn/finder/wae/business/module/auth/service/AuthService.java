package cn.finder.wae.business.module.auth.service;

import cn.finder.wae.business.domain.User;

public interface AuthService {
	
	User checkAccount(String account);
	User login(User user);
	
	
	User sessionLogin(User user);
	
	/**
	 * 用户添加
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public int addUser(String sql,Object[] parameters);
	
	/**
	 * 根据账号查找用户
	 * @param account
	 * @return
	 */
	public User findByAccount(String account);
}
