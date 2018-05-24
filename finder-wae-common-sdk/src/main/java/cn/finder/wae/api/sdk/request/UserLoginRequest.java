package cn.finder.wae.api.sdk.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.httpcommons.request.SearchRequest;
import cn.finder.httpcommons.utils.RequestValidator;
import cn.finder.wae.api.sdk.response.UserLoginResponse;

public class UserLoginRequest extends SearchRequest<UserLoginResponse>{

	@Override
	public String apiName() {
		return "wae.user.login";
	}

	@Override
	public void validate() {
		RequestValidator.validateRequired("account", getAccount()); //
        RequestValidator.validateRequired("password", getPassword()); 
		
	}

	
    private String account;


    private String password;


    @JsonProperty(name="user_name")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@JsonProperty(name="user_password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
    
    
    
}
