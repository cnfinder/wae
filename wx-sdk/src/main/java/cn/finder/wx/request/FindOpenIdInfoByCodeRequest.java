package cn.finder.wx.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.wx.response.FindOpenIdInfoByCodeResponse;

/***
 * 根据 网页授权CODE 获取用户OPENID
 * @author whl
 *
 */
public class FindOpenIdInfoByCodeRequest extends WeixinRequest<FindOpenIdInfoByCodeResponse>{

	
	private String appId;
	
	private String appSecret;
	
	private String code;
	
	private String grantType="authorization_code";

	
	@JsonProperty(name="appid")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	@JsonProperty(name="secret")
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty(name="grant_type")
	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	
	
	
}
