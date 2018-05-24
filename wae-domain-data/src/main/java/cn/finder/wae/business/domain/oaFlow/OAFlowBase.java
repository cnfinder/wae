package cn.finder.wae.business.domain.oaFlow;

import java.util.Date;

public class OAFlowBase {
	private int id;
	private String content;   
	private Date create_time;   //创建时间
	private String business_key; //业务主键
	private Date complete_time;//完成时间
	private String initiator_userid; //OA发起人
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getBusiness_key() {
		return business_key;
	}
	public void setBusiness_key(String business_key) {
		this.business_key = business_key;
	}
	public Date getComplete_time() {
		return complete_time;
	}
	public void setComplete_time(Date complete_time) {
		this.complete_time = complete_time;
	}
	public String getInitiator_userid() {
		return initiator_userid;
	}
	public void setInitiator_userid(String initiator_userid) {
		this.initiator_userid = initiator_userid;
	}
	
	

}
