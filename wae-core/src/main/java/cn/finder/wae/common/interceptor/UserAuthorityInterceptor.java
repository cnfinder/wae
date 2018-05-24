package cn.finder.wae.common.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.RequestCommand;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.common.constant.Constant;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 用户权限判断拦截器
 * @author wu hualong
 *
 */
public class UserAuthorityInterceptor  extends  MethodFilterInterceptor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5705066813857385717L;
	private static Logger logger = Logger.getLogger("");

	@Override
	public String doIntercept(ActionInvocation actionInvocation) throws Exception
	{
		
		HttpServletRequest request=ServletActionContext.getRequest();
		Map<String,Object> session=actionInvocation.getInvocationContext().getSession();
		String requestUri1=request.getRequestURI();
		User user = (User)session.get(Constant.KEY_SESSION_USER);
		if(user != null && user.getRoleId() != null){
			
			String redirectURL=requestUri1.substring(requestUri1.indexOf("/", 1)).substring(1);
			List<RequestCommand> reqs = user.getRole().getReqs();
			if(check(reqs, redirectURL)){
					return actionInvocation.invoke();
			}
		}
		return "login";
		
		
	}

	public boolean check(List<RequestCommand> reqs, String uri){
		if( reqs != null && reqs.size() > 0){
			for(RequestCommand req : reqs){
				if(uri.equals(req.getCommand())){
					return true;
				}
			}
		}
		return false;
	}
	
}
