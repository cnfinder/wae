package cn.finder.wx.corp.request;

import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.request.WeixinRequest;
import cn.finder.wx.response.WeixinResponse;

/***
 * 微信企业号更新用户
 * @author whl
 *
 */
public class WXCorpUserUpdateRequest extends JsonStringRequest<WeixinResponse>{

	
	private String userid;
	
	private String name;
	
	private String mobile;
	
	private int[] department;
	
	private int enable=1;

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

	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

	public int[] getDepartment() {
		return department;
	}

	public void setDepartment(int[] department) {
		this.department = department;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}
	
	
	

}
