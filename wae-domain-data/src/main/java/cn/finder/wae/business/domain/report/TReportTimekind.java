package cn.finder.wae.business.domain.report;

/**
 * TReportTimekind entity. @author MyEclipse Persistence Tools
 */

public class TReportTimekind implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String remark;
	private Integer state;

	// Constructors

	/** default constructor */
	public TReportTimekind() {
	}

	/** minimal constructor */
	public TReportTimekind(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public TReportTimekind(Integer id, String name, String remark, Integer state) {
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}