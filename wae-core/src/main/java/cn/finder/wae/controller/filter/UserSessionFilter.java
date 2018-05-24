package cn.finder.wae.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.common.constant.Constant;

public class UserSessionFilter  implements Filter
{
	private static Logger logger  = Logger.getLogger(UserSessionFilter.class);
	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		//此处一定要加上编码设置 ， 因为此filter放在了最前面 并且一定要放在最前面 来设置用户信息
		//request.setCharacterEncoding("UTF-8");
		
		
		HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		HttpServletResponse httpServletResponse=(HttpServletResponse)response;
		String requestUrl=httpServletRequest.getRequestURI();
		HttpSession httpSession = httpServletRequest.getSession();
		
		
		
		User user = (User)httpSession.getAttribute(Constant.KEY_SESSION_USER);
		if(user==null){
			/*user = new User();
			httpSession.setAttribute(Constant.KEY_SESSION_USER, user);*/
			//httpServletResponse.sendRedirect(httpServletRequest.getContextPath() +"/login.jsp");
			httpSession.setAttribute(Constant.KEY_SESSION_USER, user);
		}
		
		//logger.debug("user role id:"+user.getRole().getId()+" role name:"+user.getRole().getRoleName());
		
		
		chain.doFilter(request, response);
	}

	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

}