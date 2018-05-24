package cn.finder.wae.sso.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

/**
 * 拦截请求 判断是否存在用户名
 * @author finder
 *
 */
public abstract class FinderSSOFilter implements Filter {

	
	
	
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	
	 private String enabled="false";
	 private String redirectPageMode="";
	 private String signinSuccessUrl="";
	 private String signoutUrl="";
	 
	 private String casServerUrlPrefix;
	
	 
	 
	
	private void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}



	private void setEnabled(String enabled) {
		this.enabled = enabled;
	}



	private void setRedirectPageMode(String redirectPageMode) {
		this.redirectPageMode = redirectPageMode;
	}



	private void setSigninSuccessUrl(String signinSuccessUrl) {
		this.signinSuccessUrl = signinSuccessUrl;
	}



	private void setSignoutUrl(String signoutUrl) {
		this.signoutUrl = signoutUrl;
	}



	@Override
	public void destroy() {
		// TODO Auto-generated method stub

		
	}

	
	
	
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
			
		
		
		 req= (HttpServletRequest)servletRequest;
		 resp= (HttpServletResponse)servletResponse;
		 final HttpSession session=req.getSession();
		
			if("true".equals(enabled)){
				
				
				AttributePrincipal principal = (AttributePrincipal)req.getUserPrincipal();
				
				//如果 assertion!=null 表示 SSO登录成功了
			    Assertion assertion = session != null ? (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : null;

				
				
			    String loginName = null;
			    
			    //1. 如果 是 logout.action  那么就清除 cas信息
			  //  if(requestURI.indexOf(signoutUrl)!=-1){
			    	
			    	// 清除  cas client session
			    	/*Enumeration enm=session.getAttributeNames();
			    	while (enm.hasMoreElements()) {
						String attr_name = (String)enm.nextElement();
						session.removeAttribute(attr_name);
					}
			    	session.invalidate();
			    	//
			    	
			    	// 调用接口清除  cas server logout
			    	
			    	String redirect_url=casServerUrlPrefix+"logout";
			    	if("default".equals(redirectPageMode)){
			    		redirect_url+="?service="+req.getRequestURL();
					}else if("specify".equals(redirectPageMode)){
						redirect_url+="?service="+req.getContextPath()+signinSuccessUrl;
					}
			    	
			    	//跳转到登录页面
			    	resp.sendRedirect(redirect_url);*/
			 //   }
			     
			    try{
			    	 session.getAttribute("AAA");
			    	 session.getAttributeNames();
			    }
			    catch(IllegalStateException e){
			    	//Session already invalidated
			    	//跳转到登录页面
			    	
			    	resp.sendRedirect(buildRedirectUrl(req));
			    	return;
			    }
			   
			    
			     if(null!=principal){
				    loginName = principal.getName();
				   
				    if(assertion!=null && !(loginName==null || loginName.trim().length()==0)){
				    	try{
				    		process(loginName);
				    	}
				    	catch(IllegalStateException e){
				    		
					    	//跳转到登录页面
				    		resp.sendRedirect(buildRedirectUrl(req));
					    	return;
				    	}
					 
				    }
			     }
			     filterChain.doFilter(servletRequest, servletResponse);
			    
			}
			else{
				filterChain.doFilter(servletRequest, servletResponse); 
			}
		    

	}

	
	/***
	 * 构建跳转URL
	 * @return
	 */
	private String buildRedirectUrl(HttpServletRequest req){

		String redirect_url=casServerUrlPrefix+"login";
    	if("default".equals(redirectPageMode)){
    		
    		String service_url=req.getRequestURL().toString();
    		if(!(req.getQueryString()==null || req.getQueryString().trim().length()==0)){
    			service_url+="?"+req.getQueryString();
    			
    		}
    		try{
    			redirect_url+="?service="+URLEncoder.encode(service_url,"UTF-8");
    		}
    		catch(Exception ex){
    			
    		}
		}else if("specify".equals(redirectPageMode)){
			redirect_url+="?service="+req.getContextPath()+signinSuccessUrl;
		}
    	
    	return redirect_url;
    	
		
	}
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	 setEnabled(filterConfig.getInitParameter("enabled"));
		 setRedirectPageMode(filterConfig.getInitParameter("redirectPageMode"));
		 setSigninSuccessUrl(filterConfig.getInitParameter("signinSuccessUrl"));
		 setSignoutUrl(filterConfig.getInitParameter("signoutUrl"));
		  
		 setCasServerUrlPrefix(filterConfig.getInitParameter("casServerUrlPrefix"));
    }

    
    /***
     * 设置 产品自身的session原来数据状态， 这里是为了不改变原有项目结构
     * example:  
     *       if (session.getAttribute("user_name")==null){
     *       	//可以从数据中读取 ,根据 user_id 获取用户对象等 
     *           
     *       	session.setAttribute("user_name",user);
     *       }
     * @param user_id 登录的账户
     */
    public abstract void process(String user_id);
    public class ClassSecret{
    	public  void temp(){
    		
    	}
    }
}
