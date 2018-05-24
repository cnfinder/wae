package cn.finder.wx.app.response;

import cn.finder.wx.response.WeixinResponse;


public class FindSessionKeyResponse extends WeixinResponse {

	
	private String openid;
	
	private String session_key;
	
	private long expires_in=2592000; //会话有效期, 以秒为单位, 例如2592000代表会话有效期为30天

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	
	
	
	
}
