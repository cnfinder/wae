package cn.finder.wx.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.wx.response.FindAccessTokenResponse;

/***
 *获取TOKEN  
 * @author whl
 *
 */
public class FindAccessTokenRequest extends WeixinRequest<FindAccessTokenResponse>{

	
	private String grantType;
	
	private String appid;
	
	private String secret;

	@JsonProperty(name="grant_type")
	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	

}
