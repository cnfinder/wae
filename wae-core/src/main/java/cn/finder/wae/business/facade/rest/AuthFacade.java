package cn.finder.wae.business.facade.rest;

import cn.finder.wae.business.module.auth.service.AuthService;


/***
 * 提供对rest服务的数据转换 把 业务下的 domain类型 转换成 rest下domain类型
 * @author wu hualong
 *
 */
public class AuthFacade {
	
	private AuthService authService;
	
	
	
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}
	
	


	/*
	public cn.iron.psp.service.rest.model.User login(String userName,String pwd)
	{
		User user=new User();
			
		user.setUserName(userName);
		user.setUserPwd(pwd);
		user=authService.login(user);
		
		cn.iron.psis.service.rest.model.User u =new cn.iron.psis.service.rest.model.User();
		
		u.setUserId(user.getUserId());
		u.setUserName(user.getUserName());
		
		return u;
		
		return null;
	}
	*/	
	
}
