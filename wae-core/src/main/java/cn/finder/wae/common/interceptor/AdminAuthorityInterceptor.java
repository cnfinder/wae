package cn.finder.wae.common.interceptor;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.RequestCommand;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.common.constant.Constant;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 后台用户权限判断拦截器
 * @author wu hualong
 *
 */
public class AdminAuthorityInterceptor  extends  MethodFilterInterceptor
{

	

	private String authMode="normal"; //normal | sso
	
	private String casServerUrlPrefix;
	
	public void setAuthMode(String authMode){
		this.authMode=authMode;
	}
	
	
	public void setCasServerUrlPrefix(String casServerUrlPrefix){
		this.casServerUrlPrefix=casServerUrlPrefix;
	}
	
	
	private String redirectUrl="";
	
	
	
	public String getRedirectUrl() {
		return redirectUrl;
	}


	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 5705066813857385717L;
	private static Logger logger = Logger.getLogger(AdminAuthorityInterceptor.class);

	@Override
	public String doIntercept(ActionInvocation actionInvocation) throws Exception
	{
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		Map<String,Object> session=actionInvocation.getInvocationContext().getSession();
		//  /PSP/manage/manageIndex.action
		String requestUri1=request.getRequestURI();
		//   /PSP
		String contextPath = request.getContextPath();
		
		
		
		
		User user = (User)session.get(Constant.KEY_SESSION_USER);
		if(user != null && user.getRoleId() != null){
			
		//	String redirectURL=requestUri1.substring(requestUri1.indexOf("/", 1)).substring(1);
			String redirectURL = requestUri1.replace(contextPath, "");
			logger.debug("==redirectUrl:"+redirectURL);
			List<RequestCommand> reqs = user.getRole().getReqs();
			if(check(reqs, redirectURL)){
					return actionInvocation.invoke();
			}else{
				//权限不足 非法访问提示
				return "no_authority";
			}
		}
		
		String pcode=request.getParameter("pcode");
		if("sso".equals(authMode)){
			
			
			String redirect_url=buildRedirectUrl(request);
			/*setRedirectUrl(redirect_url);
			request.setAttribute("redirectUrl2", redirect_url);
			session.put("redirectUrl2", redirect_url);*/
			
			
			if(StringUtils.isEmpty(pcode)){
				return "sso_login";
			}
			else if("400".equals(pcode)){
				return "sso_400_login";
			}
			
			return "redirectUrl";
		}
		else{
			
			//response.sendRedirect(buildRedirectUrl(request));
			if(StringUtils.isEmpty(pcode)){
				return "login";
			}
			else if("400".equals(pcode)){
				return "400_login";
			}
				
			return "login";
			
		}
		
		
		
		
	}
	
	
	/***
	 * 构建跳转URL
	 * @return
	 */
	private String buildRedirectUrl(HttpServletRequest req){

		String redirect_url=casServerUrlPrefix+"login";
    		
		String service_url=req.getRequestURL().toString();
		if(!StringUtils.isEmpty(req.getQueryString())){
			service_url+="?"+req.getQueryString();
		}
		try{
			redirect_url+="?service="+URLEncoder.encode(service_url,"UTF-8");
		}
		catch(Exception ex){
			
		}
    	
    	return redirect_url;
    	
		
	}

	public boolean check(List<RequestCommand> reqs, String uri){
		if( reqs != null && reqs.size() > 0){
			for(RequestCommand req : reqs){
				if(uri.startsWith(req.getCommand())){
					return true;
				}
			}
		}
		return false;
	}
	
}
