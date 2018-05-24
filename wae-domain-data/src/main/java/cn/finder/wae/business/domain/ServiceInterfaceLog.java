package cn.finder.wae.business.domain;

import java.util.Date;

/**
 * @author: wuhualong
 * @data:20141011
 * @function:
 */
public class ServiceInterfaceLog {
	private long id;
	
	private String interfaceName;

	private String interfaceNameCn;
	
	private long showtableConfigId;
	
	private int isNeedAuth;
	
	private String groupName;
	private String version;
	private String remark;
	
	private int enabled;
	
	private int enableLog=1;
	
	private Date invokeTime;
	
	private Date completeTime;
	
	private String inputContent;
	
	private String statusCode;
	
	private String outMsg;
	


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceNameCn() {
		return interfaceNameCn;
	}

	public void setInterfaceNameCn(String interfaceNameCn) {
		this.interfaceNameCn = interfaceNameCn;
	}

	public long getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIsNeedAuth() {
		return isNeedAuth;
	}

	public void setIsNeedAuth(int isNeedAuth) {
		this.isNeedAuth = isNeedAuth;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getEnableLog() {
		return enableLog;
	}

	public void setEnableLog(int enableLog) {
		this.enableLog = enableLog;
	}

	public Date getInvokeTime() {
		return invokeTime;
	}

	public void setInvokeTime(Date invokeTime) {
		this.invokeTime = invokeTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getInputContent() {
		return inputContent;
	}

	public void setInputContent(String inputContent) {
		this.inputContent = inputContent;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getOutMsg() {
		return outMsg;
	}

	public void setOutMsg(String outMsg) {
		this.outMsg = outMsg;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return interfaceName +" "+interfaceNameCn + " "+  " "+version+ " "+inputContent+ "  "+outMsg + " "+statusCode;
	}
	
	 
	

}
