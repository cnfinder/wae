package cn.finder.wae.business.domain.oaFlow;

public class OAFlowType {
	private int id;
	private String name;
	private String remark;
	private String process_key;

	public OAFlowType() {
		super();
	}

	public OAFlowType(int id, String name, String remark, String process_key) {
		super();
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.process_key = process_key;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProcess_key() {
		return process_key;
	}

	public void setProcess_key(String process_key) {
		this.process_key = process_key;
	}

}
