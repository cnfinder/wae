package cn.finder.wae.business.domain.wx;

public class WXAppInfoManager {

	private int id;
	
	private String name;
	
	private String openid;
	private int isAuth;
	
	private int receiveFlag;
	
	private int appInfoId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public int getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}

	public int getReceiveFlag() {
		return receiveFlag;
	}

	public void setReceiveFlag(int receiveFlag) {
		this.receiveFlag = receiveFlag;
	}

	public int getAppInfoId() {
		return appInfoId;
	}

	public void setAppInfoId(int appInfoId) {
		this.appInfoId = appInfoId;
	}
	
	
}
