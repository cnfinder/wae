package cn.finder.wae.business.module.auth.dao.impl;

import java.util.List;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.module.auth.dao.AuthDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class AuthDaoImpl extends BaseJdbcDaoSupport implements AuthDao{
	
	@Override
	public List<User> findAll() {
		String sql = "select * from v_user";
		List<User> list = getJdbcTemplate().query(sql, new RowMapperFactory.UserRowMapper());
		return list;
	}

	@Override
	public User findById(Long id) {
		String sql = "select * from v_user where id = ?" ;
		List<User> list = getJdbcTemplate().query(sql,new Object[]{id}, new RowMapperFactory.UserRowMapper());
		if(list != null && 0 < list.size()){
			User user = list.get(0);
			return user;
		}
		return null;
	}

	@Override
	public User findByAccount(String account) {
		String sql = "select * from v_user where account = ?" ;
		List<User> list = getJdbcTemplate().query(sql, new Object[]{account}, new RowMapperFactory.UserRowMapper());
		
		if(list != null && 0 < list.size()){
			User user = list.get(0);
			return user;
		}
		return null;
	}

	@Override
	public User login(User user) {
		String sql = "select * from v_user where account = ? and password = ?" ;
		List<User> list = getJdbcTemplate().query(sql, new Object[]{user.getAccount(), user.getPassword()}, new RowMapperFactory.UserRowMapper());
		
		if(list != null && 0 < list.size()){
			User resultUser = list.get(0);
			return resultUser;
		}
		return null;
	}
	@Override
	public User loginByPhone(String phone,String password) {
		String sql = "select * from v_user where phone = ? and password = ?" ;
		List<User> list = getJdbcTemplate().query(sql, new Object[]{phone, password}, new RowMapperFactory.UserRowMapper());
		
		if(list != null && 0 < list.size()){
			User resultUser = list.get(0);
			return resultUser;
		}
		return null;
	}
	
	@Override
	public int addUser(String sql,Object[] parameters) {
			
			return getJdbcTemplate().update(sql, parameters);
	}

}
