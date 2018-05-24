package cn.finder.wae.wx.data;

import java.io.Serializable;
import java.util.Date;


public class AppInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 500658841886531576L;
	private int id;
	private String name;
	private String appid;
	
	private String appSecret;
	
	private String openid;
	
	private String grantType;
	
	private String accessToken; //服务器端访问临时票据
	
	private long expiresIn;
	
	
	//最后过期时间
	private long endExpires;
	
	

	private String jsapi_ticket; //公众号用于调用微信JS接口的临时票据,jsapi_ticket的有效期为7200秒
	
	
	private MerchantInfo merchantInfo;
	
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

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

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

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}


	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}
	
	
	public static class MerchantInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 9062633378268880L;

		private String merchant_id;
		
		private String merchant_userid;
		
		private String notify_url;
		
		private String merchant_key;
		

		public MerchantInfo(String merchant_id, String merchant_userid,String notify_url,String merchant_key) {
			super();
			this.merchant_id = merchant_id;
			this.merchant_userid = merchant_userid;
			this.notify_url=notify_url;
			this.merchant_key=merchant_key;
		}

		public String getMerchant_id() {
			return merchant_id;
		}

		public void setMerchant_id(String merchant_id) {
			this.merchant_id = merchant_id;
		}

		public String getMerchant_userid() {
			return merchant_userid;
		}

		public void setMerchant_userid(String merchant_userid) {
			this.merchant_userid = merchant_userid;
		}

		public String getNotify_url() {
			return notify_url;
		}

		public void setNotify_url(String notify_url) {
			this.notify_url = notify_url;
		}

		public String getMerchant_key() {
			return merchant_key;
		}

		public void setMerchant_key(String merchant_key) {
			this.merchant_key = merchant_key;
		}

	
		
		
		
	}
	
}
