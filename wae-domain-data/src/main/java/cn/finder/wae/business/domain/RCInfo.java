package cn.finder.wae.business.domain;

import java.util.Date;

public class RCInfo {

	private int id;
	
	private String rcUserId;
	private String rcPwd;
	private String targetCode;
	
	private String targetIp;
	private String createUserId;
	
	private Date createDate;
	
	private Date updateDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRcUserId() {
		return rcUserId;
	}

	public void setRcUserId(String rcUserId) {
		this.rcUserId = rcUserId;
	}

	public String getRcPwd() {
		return rcPwd;
	}

	public void setRcPwd(String rcPwd) {
		this.rcPwd = rcPwd;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
}
