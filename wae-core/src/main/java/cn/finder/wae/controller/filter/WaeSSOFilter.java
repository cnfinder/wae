package cn.finder.wae.controller.filter;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.module.auth.service.AuthService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.RoleCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.constant.Constant;
import cn.finder.wae.sso.filter.FinderSSOFilter;

/**
 * WAE SSO 过滤器
 * @author finder
 *
 */
public class WaeSSOFilter extends  FinderSSOFilter {

	
	private static Logger logger=Logger.getLogger(WaeSSOFilter.class);

	@Override
	public void process(String user_id) {
		// TODO Auto-generated method stub
		
		HttpSession session= req.getSession();
		if(session.getAttribute(Constant.KEY_SESSION_USER)==null){
			
			AuthService authService=AppApplicationContextUtil.getContext().getBean("authService", AuthService.class);
			
		    final User user = authService.findByAccount(user_id);
			RoleCache roleCache = ArchCache.getInstance().getRoleCache();
			user.setRole(roleCache.get(user.getRoleId()));
			session.setAttribute(Constant.KEY_SESSION_USER, user);
				
		}
	}

}
