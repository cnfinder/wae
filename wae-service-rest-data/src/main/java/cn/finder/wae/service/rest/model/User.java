package cn.finder.wae.service.rest.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import cn.finder.wae.service.rest.common.XsDateAdaptor;
/**
 *@author wu hualong
 */
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -931226383904067662L;
	
	@XmlElement(name="user_id")
	private int id;
	
	
	private String userName;
	
	private String password;
	
	private int isActivite;
	
	private int integral;
	
	private int integralRecharge;
	
	
	
	@XmlElement(name="birthday")
	@XmlJavaTypeAdapter(value=XsDateAdaptor.class)
	private Date birthday;
	
	@XmlElement(name="appKey",defaultValue="0xxxx")
	private String appKey;
	

	private String appSecret;
	
	private long userId;
	
	@XmlTransient
	private String desc="my desc";
	
	private String name;
	
	private int deptId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getIsActivite() {
		return isActivite;
	}
	public void setIsActivite(int isActivite) {
		this.isActivite = isActivite;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getIntegralRecharge() {
		return integralRecharge;
	}
	public void setIntegralRecharge(int integralRecharge) {
		this.integralRecharge = integralRecharge;
	}
	
	 
}
