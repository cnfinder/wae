package cn.finder.wx.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.wx.response.AccessTokenRefreshResponse;


/****
 * 
 * 刷新access_token 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，
 * refresh_token拥有较长的有效期
 * （7天、30天、60天、90天），当refresh_token失效的后，需要用户重新授权
 * @author Administrator
 *
 */
public class AccessTokenRefreshRequest extends WeixinRequest<AccessTokenRefreshResponse>{

	private String appId;
	
	private String grantType="refresh_token";
	
	private String refreshToken;

	
	@JsonProperty(name="appid")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/***
	 * 填refresh_token
	 * @return
	 */
	@JsonProperty(name="grant_type")
	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	/***
	 * 填写通过access_token获取到的refresh_token参数
	 * @return
	 */
	@JsonProperty(name="refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	
}
