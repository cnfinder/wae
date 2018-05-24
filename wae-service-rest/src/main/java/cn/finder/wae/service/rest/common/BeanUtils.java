package cn.finder.wae.service.rest.common;

import javax.servlet.http.HttpServletRequest;

import org.restlet.Request;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BeanUtils {

	public static <T> T getBean(String beanName,Class<? extends T> clazz,Request request)
	{
		HttpServletRequest httpServletRequest=CommonUtil.getRequest(request);
		return WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getSession().getServletContext()).getBean(beanName,clazz);
	}
	
	public static <T> T getBean(String beanName,Class<? extends T> clazz,HttpServletRequest httpServletRequest)
	{
		return WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getSession().getServletContext()).getBean(beanName,clazz);
	}
}
