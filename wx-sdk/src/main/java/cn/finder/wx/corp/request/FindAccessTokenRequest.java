package cn.finder.wx.corp.request;

import cn.finder.wx.corp.response.FindAccessTokenResponse;
import cn.finder.wx.request.WeixinRequest;

/***
 *获取TOKEN  
 * @author whl
 *
 */
public class FindAccessTokenRequest extends WeixinRequest<FindAccessTokenResponse>{

	
	private String corpid;
	
	private String corpsecret;

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getCorpsecret() {
		return corpsecret;
	}

	public void setCorpsecret(String corpsecret) {
		this.corpsecret = corpsecret;
	}

	
	

}
