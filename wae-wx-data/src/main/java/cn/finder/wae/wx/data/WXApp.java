package cn.finder.wae.wx.data;

import java.io.Serializable;

public  class WXApp implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3966818185181075254L;

	private String id;
	
	private String name;
	private String wx_appid;
	
	private String agentid;
	
	private String is_msg_agent;
	
	private String secret;
	
	private String access_token;
	
	private long  expires_in;
	
	
	private String jsapi_ticket;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWx_appid() {
		return wx_appid;
	}

	public void setWx_appid(String wx_appid) {
		this.wx_appid = wx_appid;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getIs_msg_agent() {
		return is_msg_agent;
	}

	public void setIs_msg_agent(String is_msg_agent) {
		this.is_msg_agent = is_msg_agent;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}
	
	
	
	
}
