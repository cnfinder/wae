package cn.finder.wae.business.domain.report;

/**
 * TReportField entity. @author MyEclipse Persistence Tools
 */

public class TReportField implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fieldName;
	private String fieldCode;
	private Short state;
	private Integer report_id;
	private String sql;
	private String connection;

	// Constructors

	/** default constructor */
	public TReportField() {
	}

	/** minimal constructor */
	public TReportField(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public TReportField(Integer id, String fieldName, String fieldCode,
			Short state) {
		this.id = id;
		this.fieldName = fieldName;
		this.fieldCode = fieldCode;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldCode() {
		return this.fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public Integer getReport_id() {
		return report_id;
	}

	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

}