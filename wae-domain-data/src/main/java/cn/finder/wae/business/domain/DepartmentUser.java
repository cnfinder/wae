package cn.finder.wae.business.domain;

import java.sql.Timestamp;

/**
 * TDepartmentUser entity. @author MyEclipse Persistence Tools
 */

public class DepartmentUser implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userId;
	private Long departmentId;
	private Timestamp createDate;
	private Timestamp updateDate;
	private String comment;

	// Constructors

	/** default constructor */
	public DepartmentUser() {
	}

	/** minimal constructor */
	public DepartmentUser(Long id) {
		this.id = id;
	}

	/** full constructor */
	public DepartmentUser(Long id, Long userId, Long departmentId,
			Timestamp createDate, Timestamp updateDate, String comment) {
		this.id = id;
		this.userId = userId;
		this.departmentId = departmentId;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.comment = comment;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
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

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}