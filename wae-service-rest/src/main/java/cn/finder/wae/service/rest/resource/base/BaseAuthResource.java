package cn.finder.wae.service.rest.resource.base;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import cn.finder.wae.business.facade.rest.AuthFacade;
import cn.finder.wae.service.rest.factory.BeanFactory;
import cn.finder.wae.service.rest.model.Response;
import cn.finder.wae.service.rest.model.User;
/**
 *@author wu hualong
 */
public abstract class BaseAuthResource extends  BaseServerResource {
	private final static Logger logger=Logger.getLogger(BaseAuthResource.class);
	//protected boolean modifiable=false;
	protected User user=new User();
	@Override
	protected void doInit() throws ResourceException {

		super.doInit();
		
		ChallengeResponse challengeResponse = getRequest()
				.getChallengeResponse();

		if (challengeResponse != null) {
			
			String userName=challengeResponse.getIdentifier();
			String pwd=new String(challengeResponse.getSecret());
			//AuthFacade auth=BeanUtils.getBean("authFacade", AuthFacade.class,getRequest());
			AuthFacade auth=BeanFactory.getAuthFacade(getRequest());
			
			//User xk_user=auth.login(userName, pwd);
			User xk_user=null;
			
			Response<Object> resp=new Response<Object>();
			
			if (xk_user==null) {
				resp.getMessage().setStatusCode(Status.CLIENT_ERROR_UNAUTHORIZED.getCode());
				resp.getMessage().setMsg("用户名或密码错误");
				
				Representation repres=toRepresentation(resp);
				logger.debug("=========用户名或密码错误");
				try {
					getResponse().setEntity(repres.getText(),variant.getMediaType());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else{
				logger.debug("=========有效的用户名和密码");
			}
			
			
		}
	}

	
	
	
}
