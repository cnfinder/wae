package cn.finder.wae.business.module.auth.service.impl;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.module.auth.dao.AuthDao;
import cn.finder.wae.business.module.auth.service.AuthService;
import cn.finder.wae.business.module.sys.dao.RoleDao;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.comm.CryptUtil;
import cn.finder.wae.common.comm.MD5Util;

public class AuthServiceImpl implements AuthService{

	private AuthDao authDao;
	
	public void setAuthDao(AuthDao authDao)
	{
		this.authDao = authDao;
	}
	
	@Override
	public User login(User user) {
		
		if(Common.isMobileNO(user.getAccount())){
			
			//手机号码登录
			return authDao.loginByPhone(user.getAccount(), MD5Util.getMD5(user.getPassword()));
		}else{
			user.setPassword(MD5Util.getMD5(user.getPassword()));
			return authDao.login(user);
		}
		
	}
	@Override
	public User checkAccount(String account) {
		if(account != null){
			User user = authDao.findByAccount(account);
			if(user != null ){
				return user;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.finder.wae.business.module.auth.service.AuthService#sessionLogin(cn.finder.wae.business.domain.User)
	 */
	@Override
	public User sessionLogin(User user) {
		return authDao.login(user);
	}

	@Override
	public int addUser(String sql, Object[] parameters) {
		
		return authDao.addUser(sql, parameters);
	}

	@Override
	public User findByAccount(String account) {
		
		return authDao.findByAccount(account);
	}


}
