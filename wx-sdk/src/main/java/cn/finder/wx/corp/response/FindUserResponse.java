package cn.finder.wx.corp.response;

import cn.finder.wx.response.WeixinResponse;

public class FindUserResponse extends WeixinResponse {
	
	
	private String userid;
	
	private String name;
	
	private String mobile;
	
	private Integer[] department;
	
	

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer[] getDepartment() {
		return department;
	}

	/*public void setDepartment(Integer[] department) {
		this.department = department;
	}
	public void setDepartment(int[] department) {
		if(department!=null){
			this.department=new Integer[department.length];
			for(int i=0;i<department.length;i++){
				this.department[i]=department[i];
			}
		}
	}*/
	public void setDepartment(Object[] department) {
		if(department!=null){
			this.department=new Integer[department.length];
			for(int i=0;i<department.length;i++){
				this.department[i]=Integer.valueOf(department[i].toString());
			}
		}
	}
	
	
	
}
