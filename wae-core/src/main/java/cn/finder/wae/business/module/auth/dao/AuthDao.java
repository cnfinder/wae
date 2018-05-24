package cn.finder.wae.business.module.auth.dao;

import java.util.List;

import cn.finder.wae.business.domain.User;

public interface AuthDao {
	
	public List<User> findAll();
	public User findById(Long id);
	public User findByAccount(String account);
	public User login(User user);
	public User loginByPhone(String phone,String password);
	
	/**
	 * 用户添加
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public int addUser(String sql,Object[] parameters);
	
}
