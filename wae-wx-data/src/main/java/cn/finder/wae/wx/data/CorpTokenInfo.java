package cn.finder.wae.wx.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 企业号访问票据
 * @author whl
 *
 */
public class CorpTokenInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1263294804536038318L;
	private String corpid;
	private String corpsecret;
	
	private String accessToken; //基于管理权限
	
	private long expiresIn;

	//最后过期时间
	private long endExpires;
	
	
	private String jsapi_ticket; //企业号用于调用微信JS接口的临时票据,jsapi_ticket的有效期为7200秒
	
	private int adminGroupTypeId; //管理员组类型ID
	
	private String adminGroupTypeName;//管理员组类型名称
	
	private String adminGroupTypeCode;//管理员组 类型编码
	
	private List<WXApp> wxApps=new ArrayList<WXApp>(); // wx app
	
	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getCorpsecret() {
		return corpsecret;
	}

	public void setCorpsecret(String corpsecret) {
		this.corpsecret = corpsecret;
	}

	/***
	 * 权限细分到 app级别
	 */
	@Deprecated
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
		
		long dur= new Date().getTime()+expiresIn*1000;
		
		endExpires =new Date(dur).getTime();
	}

	public long getEndExpires() {
		return endExpires;
	}

	public void setEndExpires(long endExpires) {
		this.endExpires = endExpires;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public int getAdminGroupTypeId() {
		return adminGroupTypeId;
	}

	public void setAdminGroupTypeId(int adminGroupTypeId) {
		this.adminGroupTypeId = adminGroupTypeId;
	}

	public String getAdminGroupTypeName() {
		return adminGroupTypeName;
	}

	public void setAdminGroupTypeName(String adminGroupTypeName) {
		this.adminGroupTypeName = adminGroupTypeName;
	}

	public String getAdminGroupTypeCode() {
		return adminGroupTypeCode;
	}

	public void setAdminGroupTypeCode(String adminGroupTypeCode) {
		this.adminGroupTypeCode = adminGroupTypeCode;
	}
	
	
	
	
	public List<WXApp> getWxApps() {
		return wxApps;
	}

	@Deprecated
	public void setWxApps(List<WXApp> wxApps) {
		this.wxApps = wxApps;
	}
	
	public  void addApp(WXApp wxApp){
		
		synchronized(wxApp){
			
			boolean isExist=false;
			for(WXApp item:wxApps){
				if(item.getId()==wxApp.getId()){
					//替换
					item = wxApp;
					isExist=true;
					break;
				}
				
			}
			if(!isExist){
				wxApps.add(wxApp);
			}
			
			
		}
		
	}


	/***
	 * 获取消息应用的  agentid
	 * @return
	 */
	public String getMsgAgentId(){
		String agentid="0";
		if(wxApps==null || wxApps.size()==0){
			
			agentid="0";
		}else{
			
			for(WXApp wxapp:wxApps){
				if("Y".equalsIgnoreCase(wxapp.getIs_msg_agent())){
					//消息app
					agentid=wxapp.getAgentid();
					break;
				}
			}
			
		}
		return agentid;
	}


	/***
	 * 获取企业号的 App 信息
	 * @param agentid
	 * @return
	 */
	public WXApp getWxApp(String agentid){
		
		for(WXApp wxapp:wxApps){
			
			if(wxapp.getAgentid().equals(agentid)){
				return wxapp;
			}
		}
		return null;
		
	}
	
}
