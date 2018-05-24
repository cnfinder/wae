package cn.finder.wae.business.domain.oaFlow;

public class OAFlowItem {

	private int id;

	private String flowName;

	private String procedefProcessKey;

	private OAFlowType oaFlowType;

	private String flowDesc;

	public String getFlowDesc() {
		return flowDesc;
	}

	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}

	public OAFlowType getOaFlowType() {
		return oaFlowType;
	}

	public void setOaFlowType(OAFlowType oaFlowType) {
		this.oaFlowType = oaFlowType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getProcedefProcessKey() {
		return procedefProcessKey;
	}

	public void setProcedefProcessKey(String procedefProcessKey) {
		this.procedefProcessKey = procedefProcessKey;
	}

}
