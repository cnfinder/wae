package cn.finder.wae.service.rest.factory;

import org.restlet.Request;

import cn.finder.wae.business.facade.rest.AuthFacade;
import cn.finder.wae.business.facade.rest.PspFacade;
import cn.finder.wae.service.rest.common.BeanUtils;


public class BeanFactory {

	public static AuthFacade getAuthFacade(Request request)
	{ 
		return BeanUtils.getBean("authFacade", AuthFacade.class, request);
	}
	
	
	
	public static PspFacade getPspFacade(Request request)
	{
		return BeanUtils.getBean("pspFacade", PspFacade.class, request);
	}
	
}
