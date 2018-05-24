package cn.finder.wx.response;

import cn.finder.httpcommons.attri.JsonProperty;


public class AccessTokenRefreshResponse extends WeixinResponse {

	private String accessToken;
	
	private long expiresIn;
	
	private String refreshToken;
	
	private String openId;
	
	private String scope;

	@JsonProperty(name="access_token")
	public String getAccessToken() {
		return accessToken;
	}

	
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty(name="expires_in")
	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	@JsonProperty(name="refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	
	@JsonProperty(name="openid")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@JsonProperty(name="scope")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}
