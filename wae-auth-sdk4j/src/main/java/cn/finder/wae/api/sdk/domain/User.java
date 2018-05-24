package cn.finder.wae.api.sdk.domain;

import java.util.Date;

import cn.finder.httpcommons.ApiObject;

/***
 * 用户信息
 * @author whl
 *
 */
public class User extends ApiObject
{
    private long id;

    private String name;
    private String account;

    private String password;
    private String email;

    private String phone;

    private String address;

    private int age;

    private int gender;

    private long roleId;

    private Date birthday;
    
    private	String	department_name;
	private	String	role_name;
	private String sessionId;
	
	private String role_code;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public	String	getDepartment_name()
	{
		return 	department_name;
	}
	public void setDepartment_name(String department_name)
	{
		this.department_name=department_name;
	}

	public	String	getRole_name()
	{
		return 	role_name;
	}
	public void setRole_name(String role_name)
	{
		this.role_name=role_name;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}
	
	
}
