package cn.finder.wx.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.wx.response.CheckAuthAccessTokenResponse;
import cn.finder.wx.response.FindUserInfoResponse;

/***
 * 
 * 检验授权凭证（access_token）是否有效
 * @author whl
 *http：GET（请使用https协议） https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID 
 */
public class CheckAuthAccessTokenRequest extends WeixinRequest<CheckAuthAccessTokenResponse>{

	
	private String accessToken; //网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	
	private String openId;

	/***
	 * 调用凭证
	 * @return
	 */
	@JsonProperty(name="access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	
	/***
	 * 普通用户的标识，对当前开发者账号唯一
	 * @return
	 */
	@JsonProperty(name="openid")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	
	
	
}
