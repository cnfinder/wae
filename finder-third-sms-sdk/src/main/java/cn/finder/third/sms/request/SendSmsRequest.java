package cn.finder.third.sms.request;

import cn.finder.httpcommons.request.DefaultRequest;
import cn.finder.third.sms.response.SendSmsResponse;

/***
 * 根据 网页授权CODE 获取用户OPENID
 * @author whl
 *
 */
public class SendSmsRequest extends DefaultRequest<SendSmsResponse>{

	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	private String userid;
	
	private String account="";
	private String password;
	private String mobile;
	private String content;
	private String sendTime="";
	private String action="send";
	
	private String extno="";

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExtno() {
		return extno;
	}

	public void setExtno(String extno) {
		this.extno = extno;
	}
	
	
	
	
}
