package cn.finder.wae.api.sdk.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.httpcommons.request.SearchRequest;
import cn.finder.wae.api.sdk.response.UserSearchResponse;

public class UserSearchRequest extends SearchRequest<UserSearchResponse>{

	@Override
	public String apiName() {
		return "wae.user.search";
	}

	@Override
	public void validate() {
		
	}
	
	private String account;

	@JsonProperty(name="user_name")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	

	
}
