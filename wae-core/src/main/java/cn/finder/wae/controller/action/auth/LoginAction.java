package cn.finder.wae.controller.action.auth;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;

import cn.finder.wae.business.domain.Log;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.module.auth.service.AuthService;
import cn.finder.wae.business.module.auth.service.LogService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.RoleCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.common.comm.WebUtil;
import cn.finder.wae.common.constant.Constant;

public class LoginAction  extends BaseActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger=Logger.getLogger(LoginAction.class);
	
	private AuthService authService;
	
	private User user;
	
	private String authCode;
	
	private String msg;
	
	private String pcode="101";//平台页面编码
	
	private LogService logService;
	
	private String goPage;
	
	
	
	public void setAuthService(AuthService authService)
	{
		this.authService= authService;
	}
	
	public String loginInput()
	{
		
		
		if(StringUtils.isEmpty(pcode)){
			pcode="101";
		}
		logger.info("====pageindex size:"+ ArchCache.getInstance().getPageIndexCache().get().size());
		goPage=ArchCache.getInstance().getPageIndexCache().get(pcode).getLoginPage();
		
		return SUCCESS;
	}
	
	
	
	
	
	
	
	//登录页面 
	public String input()
	{
		return SUCCESS;
	}
	
	//execute login operation
	 
	public String login1(){
		String isImgValidate = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_SHOW_LOGIN_VALIDATE_IMG).getValue();
		if(isImgValidate.equals("1")){
			String code = (String)session.getAttribute("rand");
			if(!authCode.equalsIgnoreCase(code)){
				this.setMsg("验证码输入错误！");
				return INPUT;
			}
		}
		User resultUser = authService.login(user);
		if (resultUser != null) {
			user = resultUser;
			RoleCache roleCache = ArchCache.getInstance().getRoleCache();
			user.setRole(roleCache.get(user.getRoleId()));
			session.setAttribute(Constant.KEY_SESSION_USER, user);

			return "login_success";
		}
		this.setMsg("用户名或密码错误！");
		return INPUT;
	//	this.setMsg("该用户不存在！");
//		return INPUT;
	}
	
	
	 
		public String login(){
			String isImgValidate = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_SHOW_LOGIN_VALIDATE_IMG).getValue();
			if(isImgValidate.equals("1")){
				String code = (String)session.getAttribute("rand");
				if(authCode==null ||code ==null ||!authCode.equalsIgnoreCase(code)){
					this.setMsg("验证码输入错误！");
				}
			}
			User resultUser = authService.login(user);
			if (resultUser != null) {
				user = resultUser;
				RoleCache roleCache = ArchCache.getInstance().getRoleCache();
				user.setRole(roleCache.get(user.getRoleId()));
				session.removeAttribute(Constant.KEY_SESSION_USER);
				session.setAttribute(Constant.KEY_SESSION_USER, user);
				Log lastLog = logService.getLastLog(user.getId());
				user.setLastLog(lastLog);
			}
			
			else
				this.setMsg("用户名或密码错误！");
		
			//多线程处理获取用户登陆信息
			Thread td1 = new Thread(new Runnable() {
				
				@Override
				public void run() {

						//登陆日志
						Log log=new Log();
						
						log.setUserName(user.getAccount());
						
						//
						final HttpServletRequest theRequest = request;
						if(theRequest!=null){
							log.setiP(WebUtil.getIp(theRequest));
						}else{
							log.setiP("unknown");
						}
						
						Date date = new Date();
						//if(user!=null)
							//log.setUser_id(user.getId().intValue());
						log.setLogDate(date);
						log.setSessionId(session.getId());
						log.setLogtype(1);
						logService.add(log);
						
				}
			});
			td1.start();
			
			return SUCCESS;
		}
		
	public String loginBackgroud()
	{
		User resultUser = authService.login(user);
		if (resultUser != null) {
			user = resultUser;
			RoleCache roleCache = ArchCache.getInstance().getRoleCache();
			user.setRole(roleCache.get(user.getRoleId()));
			session.setAttribute(Constant.KEY_SESSION_USER, user);
			Log lastLog = logService.getLastLog(user.getId());
			user.setLastLog(lastLog);
		}
		
		else
			this.setMsg("用户名或密码错误！");
	
		//多线程处理获取用户登陆信息
		Thread td1 = new Thread(new Runnable() {
			
			@Override
			public void run() {

					//登陆日志
					Log log=new Log();
					
					log.setUserName(user.getAccount());
					
					//
					final HttpServletRequest theRequest = request;
					if(theRequest!=null){
						log.setiP(WebUtil.getIp(theRequest));
					}else{
						log.setiP("unknown");
					}
					
					Date date = new Date();
					//if(user!=null)
						//log.setUser_id(user.getId().intValue());
					log.setLogDate(date);
					log.setSessionId(session.getId());
					log.setLogtype(1);
					logService.add(log);
					
			}
		});
		
		return SUCCESS;
	}
	
	public String logOut(){
		session.setAttribute(Constant.KEY_SESSION_USER, null);
		
		if(StringUtils.isEmpty(pcode)){
			pcode="101";
		}
		
		goPage=ArchCache.getInstance().getPageIndexCache().get(pcode).getLoginPage();
		
		Thread td1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		return SUCCESS;
	}
	
	@JSON(serialize=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getGoPage() {
		return goPage;
	}

	public void setGoPage(String goPage) {
		this.goPage = goPage;
	}

	public String getPcode() {
		return pcode;
	}

	
	

	 




	
}
