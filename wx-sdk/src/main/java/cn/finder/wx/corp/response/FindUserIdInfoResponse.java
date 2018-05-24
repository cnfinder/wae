package cn.finder.wx.corp.response;

import cn.finder.httpcommons.attri.JsonProperty;
import cn.finder.wx.response.WeixinResponse;

public class FindUserIdInfoResponse extends WeixinResponse {

	private String userId;
	private String deviceId;
	
	private String openId;

	public String getUserId() {
		return userId;
	}

	@JsonProperty(name="UserId")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getDeviceId() {
		return deviceId;
	}

	@JsonProperty(name="DeviceId")
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOpenId() {
		return openId;
	}
	@JsonProperty(name="OpenId")
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	/***
	 * 是否调用出错了
	 * 
	 * @return
	 */
	public boolean isError(){
		if("40029".equals(getErrcode())){
			return true;
		}
		return false;
	}
	
	/***
	 * 是否是企业用户 如果是 获取 getUserId  否则 获取getOpenId
	 * @return
	 */
	public boolean isCorpUser(){
		if(!isError()){
			if(userId!=null &&userId.length()>0){
				return true;
			}else if(openId!=null &&openId.length()>0){
				return false;
			}
		}
		return false;
	}
	
}
