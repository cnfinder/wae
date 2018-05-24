package cn.finder.wx.corp.response;

import cn.finder.wx.response.WeixinResponse;


public class FindJsapiTicketResponse extends WeixinResponse {

	
	private String ticket;
	
	private long expires_in;

	

	

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	
}
