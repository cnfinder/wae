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

import cn.finder.wae.common.thread.AppContent;

/**
 * 把当前请求的 request、response对象设置到当前上下文中
 * @author wuhualong
 *
 */
public class FindContextFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		    AppContent.setRequest((HttpServletRequest) servletRequest);  
		    AppContent.setResponse((HttpServletResponse) servletResponse);  
		    filterChain.doFilter(servletRequest, servletResponse);  

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
