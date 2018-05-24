package cn.finder.wae.common.base;

import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("all")
public class BaseActionMapSupport extends ActionSupport implements RequestAware,SessionAware
{
	
	private static final long serialVersionUID = 1968038891450289249L;
	
	protected Map<String,Object> request;
	protected Map<String,Object> session;
	@Override
	public void setRequest(Map session) {
		this.session=session;
		
	}
	@Override
	public void setSession(Map request) {
		this.request=request;
		
	}
	
	/*@Override
	public void setSession(Map<String, Object> session)
	{
		this.session=session;
	}

	@Override
	public void setRequest(Map<String, Object> request)
	{
		this.request=request;
	}*/

}
