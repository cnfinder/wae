package cn.finder.wae.business.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TRole entity. @author MyEclipse Persistence Tools
 */

public class Role implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Date createDate;
	private Date updateDate;
	private Long parentId;
	private String roleCode;

	private List<Menu> menus=new ArrayList<Menu>();
	private List<RequestCommand> reqs=new ArrayList<RequestCommand>();
	
	private Role parent;
	private List<Role> children;
	// Constructors

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Role(Long id, String name, Date createDate,
			Date updateDate) {
		this.id = id;
		this.name = name;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public List<RequestCommand> getReqs() {
		return reqs;
	}

	public void setReqs(List<RequestCommand> reqs) {
		this.reqs = reqs;
	}

	public Role getParent() {
		return parent;
	}

	public void setParent(Role parent) {
		this.parent = parent;
	}

	public List<Role> getChildren() {
		return children;
	}

	public void setChildren(List<Role> children) {
		this.children = children;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	
	
}