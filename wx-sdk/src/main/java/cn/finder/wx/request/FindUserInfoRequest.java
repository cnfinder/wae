package cn.finder.wx.request;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.wx.response.FindUserInfoResponse;

/***
 * 
 * 通过access_token拉取用户信息(仅限scope= snsapi_userinfo)：
 * @author whl
 *https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN?access_token=ACCESS_TOKEN&openid=OPENID
 */
public class FindUserInfoRequest extends WeixinRequest<FindUserInfoResponse>{

	
	private String accessToken;
	
	private String openId;
	
	private String lang="zh_CN";
	
	

	
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

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	
	
}
