package cn.finder.wae.business.domain.report;

/**
 * TReportRow entity. @author MyEclipse Persistence Tools
 */

public class TReportRow implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer reportId;

	// Constructors

	/** default constructor */
	public TReportRow() {
	}

	/** minimal constructor */
	public TReportRow(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public TReportRow(Integer id, Integer reportId) {
		this.id = id;
		this.reportId = reportId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReportId() {
		return this.reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

}