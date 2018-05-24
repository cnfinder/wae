package cn.finder.wae.common.base;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class BaseDwr {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext application;
	private WebContext context;

	public void initContext() {
		context = WebContextFactory.get();
	}

	/**
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		if (request == null) {
			if (context == null) {
				initContext();
			}
			request = context.getHttpServletRequest();
		}
		return request;
	}
	
	public HttpSession getSession()
	{
		return getContext().getSession();
	}

	/**
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		if (response == null) {
			if (context == null) {
				initContext();
			}
			response = context.getHttpServletResponse();
		}
		return response;
	}

	/**
	 * 
	 * @return
	 */
	public ServletContext getApplication() {
		if (application == null) {
			if (context == null) {
				initContext();
			}
			application = context.getServletContext();
		}
		return application;
	}

	/**
	 * 
	 * @return
	 */
	public WebContext getContext() {
		if (context == null) {
			initContext();
		}
		return context;
	}

}
