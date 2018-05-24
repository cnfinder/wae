package cn.finder.wae.business.domain;

import java.util.Date;

/**
 * TUser entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1096382389882716237L;
	private Long id;
	private String name;
	private String account;
	private String password;
	private String email;
	private String phone;
	private String address;
	private Integer age;
	private Integer gender;
	private Long roleId;
	private Date createDate;
	private Date updateDate;
	private Long dptId;
	private String sessionId;
	private String role_code;
	
	
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	private Date birthday;
	private Role role;
	
	private Log lastLog;
	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(Long id) {
		this.id = id;
	}

	/** full constructor */
	public User(Long id, String name, String account, String password,
			String email, String phone, String address, Integer age,
			Integer gender, Long roleId, Date createDate,
			Date updateDate, Role role,Long dptId ) {
		this.id = id;
		this.name = name;
		this.account = account;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.age = age;
		this.gender = gender;
		this.roleId = roleId;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.role = role;
		this.dptId = dptId;
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

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Log getLastLog() {
		return lastLog;
	}

	public void setLastLog(Log lastLog) {
		this.lastLog = lastLog;
	}

	public Long getDptId() {
		return dptId;
	}

	public void setDptId(Long dptId) {
		this.dptId = dptId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}

	
	
}