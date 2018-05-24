package cn.finder.wx.response;

import java.util.List;

import cn.finder.httpcommons.attri.JsonProperty;

public class FindUserInfoLocalResponse extends WeixinResponse{

	private int subscribe;
	
	private String openid;
	
	private String nickname;
	
	private String sex;
	
	private String province;
	
	private String city;
	
	private String country;
	
	private List<String> privilege;
	
	private String headimgurl;
	
	private String unionid;
	
	private String remark;

	
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@JsonProperty(name="openid")
	public String getOpenid() {
		return openid;
	}
	@JsonProperty(name="openid")
	public void setOpenid(String openId) {
		this.openid = openId;
	}

	@JsonProperty(name="nickname")
	public String getNickname() {
		nickname=nickname.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*"); 
		
		return nickname;
	}
	@JsonProperty(name="nickname")
	public void setNickname(String nickName) {
		this.nickname = nickName;
	}

	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getPrivilege() {
		return privilege;
	}

	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	
	
	
}
