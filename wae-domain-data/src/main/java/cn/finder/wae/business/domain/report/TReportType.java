package cn.finder.wae.business.domain.report;

/**
 * TReportType entity. @author MyEclipse Persistence Tools
 */

public class TReportType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String remark;
	private Short state;

	// Constructors

	/** default constructor */
	public TReportType() {
	}

	/** minimal constructor */
	public TReportType(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public TReportType(Integer id, String name, String remark, Short state) {
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

	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

}