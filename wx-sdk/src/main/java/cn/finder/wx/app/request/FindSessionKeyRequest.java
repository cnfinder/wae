package cn.finder.wx.app.request;

import cn.finder.wx.app.response.FindSessionKeyResponse;
import cn.finder.wx.request.WeixinRequest;

/***
 * 根据CODE获取openid 和 session_key
 * @author whl
 *
 */
public class FindSessionKeyRequest extends WeixinRequest<FindSessionKeyResponse>{

	/***
	 * 小程序唯一标识
	 */
	private String appid;
	
	/***
	 * 小程序的 app secret
	 */
	private String secret;
	
	
	/***
	 * 登录时获取的 code
	 */
	private String js_code;
	
	
	private String grant_type="authorization_code";


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


	public String getJs_code() {
		return js_code;
	}


	public void setJs_code(String js_code) {
		this.js_code = js_code;
	}


	public String getGrant_type() {
		return grant_type;
	}
	
	
	
	
	

}
