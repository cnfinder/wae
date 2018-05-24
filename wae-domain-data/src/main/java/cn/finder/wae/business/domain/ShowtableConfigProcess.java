package cn.finder.wae.business.domain;

/**
 * @author: wuhualong
 * @data:2014-7-16上午11:35:29
 * @function:
 */
public class ShowtableConfigProcess {

	private long showtableConfigId;
	
	private String processKey;
	
	private String businessKey;
	
	private String remark;

	public long getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
