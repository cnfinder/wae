package cn.finder.wae.api.sdk.request;

import cn.finder.httpcommons.request.StreamRequest;
import cn.finder.httpcommons.response.ApiResponse;
import cn.finder.httpcommons.utils.RequestValidator;

public class FindUserImgRequest extends StreamRequest<ApiResponse>{

	@Override
	public String apiName() {
		return "wae.user.pic.get";
	}

	@Override
	public void validate() {
		 RequestValidator.validateRequired("account", account);
		
	}
	
	private String account;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	

	

	
}
