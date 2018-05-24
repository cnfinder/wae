package cn.finder.wae.business.domain.report;

import java.sql.Timestamp;

/**
 * TReportFieldContent entity. @author MyEclipse Persistence Tools
 */

public class TReportFieldContent implements java.io.Serializable {

	// Fields

	private Integer id;
	private String value;
	private Timestamp createDate;
	private Timestamp updateDate;
	private Short state;

	// Constructors

	/** default constructor */
	public TReportFieldContent() {
	}

	/** minimal constructor */
	public TReportFieldContent(Integer id, Timestamp createDate,
			Timestamp updateDate) {
		this.id = id;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	/** full constructor */
	public TReportFieldContent(Integer id, String value, Timestamp createDate,
			Timestamp updateDate, Short state) {
		this.id = id;
		this.value = value;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

}