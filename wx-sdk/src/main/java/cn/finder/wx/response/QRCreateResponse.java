package cn.finder.wx.response;


public class QRCreateResponse extends WeixinResponse {

	
	private String ticket;
	
	private long expire_seconds;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public long getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(long expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	

	
	
}
