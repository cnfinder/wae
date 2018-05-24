package cn.finder.wae.business.domain.ble;

import java.util.Date;

public class BleEquipment {

	private int id;
	
	private String name;
	
	private String equipmentCode;
	private String uuid;
	private String major;
	
	private String minor;
	
	private String locAddress;
	
	private Date createTime;
	
	private int isActivity;
	
	private int isEnabled;
	private String remark;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getMinor() {
		return minor;
	}
	public void setMinor(String minor) {
		this.minor = minor;
	}
	public String getLocAddress() {
		return locAddress;
	}
	public void setLocAddress(String locAddress) {
		this.locAddress = locAddress;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getIsActivity() {
		return isActivity;
	}
	public void setIsActivity(int isActivity) {
		this.isActivity = isActivity;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
}
