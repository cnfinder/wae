package cn.finder.wx.corp.domain;


/***
 * 微信用户信心
 * @author whl
 *
 */
public class User {

	private String userid;
	private String name;
	
	private Integer[] department;
	
	private String position;
	
	private String mobile;
	
	private String gender;
	
	private String email;
	private String weixinid;
	private String avatar;
	private int status;
	
	private String extattr;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	


	
	public Integer[] getDepartment() {
		return department;
	}

	/*public void setDepartment(Integer[] department) {
		this.department = department;
	}*/
	
	public void setDepartment(Object[] department) {
		if(department!=null){
			this.department=new Integer[department.length];
			for(int i=0;i<department.length;i++){
				this.department[i]=Integer.valueOf(department[i].toString());
			}
		}
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeixinid() {
		return weixinid;
	}

	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExtattr() {
		return extattr;
	}

	public void setExtattr(String extattr) {
		this.extattr = extattr;
	}
	
	
	
	
}
