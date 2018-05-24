package cn.finder.wae.common.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.finder.wae.helper.EmailHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionInterceptor extends AbstractInterceptor {  
	  
    
    private static final long serialVersionUID = 1008901298342362080L;  
    private static final Logger log = Logger  
            .getLogger(ExceptionInterceptor.class);  
    private String authMode="normal"; //normal | sso
    
    
    public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}


	@Override  
    public String intercept(ActionInvocation invocation) throws Exception {  
        String actionName = invocation.getInvocationContext().getName();  
        try {  
            String result = invocation.invoke();
            
            return result;  
        } catch (Exception e) {  
            log.error(e);
            StringBuffer sb = new StringBuffer();
            sb.append(e.toString());
            sb.append("<br/>");
            sb.append("actionName:"+actionName);
            sb.append("<br/>");
            sb.append("action:"+invocation.getAction());
            sb.append("<br/>");
            try{
            	sb.append("value stack:"+invocation.getInvocationContext().getValueStack().getContext().toString());
            }
            catch(IllegalStateException ee){
            	HttpServletRequest request=ServletActionContext.getRequest();
                String pcode=request.getParameter("pcode");
            	if("sso".equals(authMode)){
        			if(StringUtils.isEmpty(pcode)){
        				return "sso_login";
        			}
        			else if("400".equals(pcode)){
        				return "sso_400_login";
        			}
        			
        			return "sso_login";
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
            catch(Exception ee){
            	
            }
            sb.append("<br/>");
            new EmailHelper().sendEmailToAdminInThread("WAE全局异常", sb.toString());
            
            
            
            return "error";
    		
        }  
    }  
	

}  
