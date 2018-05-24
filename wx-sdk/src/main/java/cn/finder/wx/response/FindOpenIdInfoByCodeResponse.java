package cn.finder.wx.response;

import cn.finder.httpcommons.attri.JsonProperty;


public class FindOpenIdInfoByCodeResponse extends WeixinResponse{

	private String accessToken;
	
	private long expiresIn;
	
	private String refreshToken;
	
	private String openId;
	
	private String scope;

	
	
	public String getAccessToken() {
		return accessToken;
	}

	@JsonProperty(name="access_token")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	
	public long getExpiresIn() {
		return expiresIn;
	}
	@JsonProperty(name="expires_in")
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	
	
	public String getRefreshToken() {
		return refreshToken;
	}
	@JsonProperty(name="refresh_token")
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	
	
	public String getOpenId() {
		return openId;
	}

	@JsonProperty(name="openid")
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	
	public String getScope() {
		return scope;
	}
	@JsonProperty(name="scope")
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
	
}
