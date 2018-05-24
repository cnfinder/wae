package cn.finder.wx.corp.response;

import cn.finder.wx.response.WeixinResponse;

public class MsgSendResponse extends WeixinResponse {

	
	private String invaliduser;
	
	private String invalidparty;
	
	private String invalidtag;

	

	public String getInvaliduser() {
		return invaliduser;
	}

	public void setInvaliduser(String invaliduser) {
		this.invaliduser = invaliduser;
	}

	public String getInvalidparty() {
		return invalidparty;
	}

	public void setInvalidparty(String invalidparty) {
		this.invalidparty = invalidparty;
	}

	public String getInvalidtag() {
		return invalidtag;
	}

	public void setInvalidtag(String invalidtag) {
		this.invalidtag = invalidtag;
	}
	
	
}
