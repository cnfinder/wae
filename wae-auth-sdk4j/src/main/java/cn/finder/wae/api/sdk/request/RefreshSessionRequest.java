package cn.finder.wae.api.sdk.request;

import cn.finder.httpcommons.request.DefaultRequest;
import cn.finder.wae.api.sdk.response.RefreshSessionResponse;

public class RefreshSessionRequest extends DefaultRequest<RefreshSessionResponse>{

	@Override
	public String apiName() {
		return "wae.user.auth.refreshsession";
	}

	@Override
	public void validate() {
		
	}

	
    private String session;


	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}


    
    
    
    
    
    
}
