package cn.finder.wae.business.domain.report;

import java.sql.Timestamp;

/**
 * TReport entity. @author MyEclipse Persistence Tools
 */

public class TReport implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer number;
	private String url;
	private Short state;
	private String remark;
	private Short type;
	private Short kind;
	private String name;
	private Timestamp lastUpdate;
	private String limitField;
	private int showtableConfigId;

	// Constructors

	public int getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(int showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}



	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/** default constructor */
	public TReport() {
	}

	/** minimal constructor */
	public TReport(Integer id) {
		this.id = id;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Short getKind() {
		return kind;
	}

	public void setKind(Short kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLimitField() {
		return limitField;
	}

	public void setLimitField(String limitField) {
		this.limitField = limitField;
	}

}