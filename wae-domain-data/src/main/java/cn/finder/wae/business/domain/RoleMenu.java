package cn.finder.wae.business.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 */

public class RoleMenu implements java.io.Serializable {

	// Fields

	private Long id;
	private Long roleId;
	private Long menuId;
	private Date createDate;
	private Date updateDate;
	
	private Role role;
	private Menu menu;
	// Constructors

	/** default constructor */
	public RoleMenu() {
	}

	/** minimal constructor */
	public RoleMenu(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RoleMenu(Long id, Long roleId, Long menuId, Timestamp createDate,
			Timestamp updateDate) {
		this.id = id;
		this.roleId = roleId;
		this.menuId = menuId;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	
}