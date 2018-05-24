package cn.finder.wae.business.domain;

public class Constants {

	private long id;
	
	private String name;
	private String nameAlias;
	
	private long constantsGroupId;
	
	private String groupName;
	
	private String remark;
	
	private String value;

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

	public String getNameAlias() {
		return nameAlias;
	}

	public void setNameAlias(String nameAlias) {
		this.nameAlias = nameAlias;
	}

	public long getConstantsGroupId() {
		return constantsGroupId;
	}

	public void setConstantsGroupId(long constantsGroupId) {
		this.constantsGroupId = constantsGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
