package cn.finder.wae.business.domain;

/**
 * @author: wuhualong
 * @data:2014-4-16上午8:37:34
 * @function:
 */
public class ServiceInterface {
	private int id;
	
	private String interfaceName;

	private String interfaceNameCn;
	
	private long showtableConfigId;
	
	private int isNeedAuth;
	
	private String version;
	private String remark;
	
	private int enabled;
	
	
	private String groupName;
	
	private int enableLog=1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	

}
