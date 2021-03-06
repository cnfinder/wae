package cn.finder.wae.business.domain;

public class SysConfig {

	private long id;
	
	private String name;
	
	private String value;
	private String cnName;
	
	private String desc;
	
	private String groupId;
	
	private int isSecret;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	
	public int getIntValue()
	{
		return Integer.valueOf(value);
	}
	
	public boolean getBooleanValue()
	{
		return Boolean.valueOf(value);
	}
	
	public long getLongValue()
	{
		return Long.valueOf(value);
	}
	
	

	public void setValue(String value) {
		this.value = value;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIsSecret() {
		return isSecret;
	}

	public void setIsSecret(int isSecret) {
		this.isSecret = isSecret;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
}
