package cn.finder.wae.business.domain.wx;

import java.io.Serializable;

/***
 * 微信 app信息
 * @author whl
 *
 */
public class AppInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7614439279075333421L;
	private int id;
	private String name;
	private String appid;
	
	private String appSecret;
	
	private String openid;
	
	private String grantType;
	
	private String headimgurl;
	
	private int type=0;
	
	
	private MerchantInfo merchantInfo;
	

	
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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
	
	
	
	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}



	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}



	public static class MerchantInfo{
		private String merchant_id;
		
		private String merchant_userid;
		
		private String notify_url;
		
		private String merchant_key;
		
		
		
		
		public MerchantInfo() {
			super();
		}


		public MerchantInfo(String merchant_id, String merchant_userid,String notify_url,String merchant_key) {
			super();
			this.merchant_id = merchant_id;
			this.merchant_userid = merchant_userid;
			this.notify_url=notify_url;
			this.merchant_id=merchant_key;
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


		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "merchant_id="+merchant_id+",merchant_userid="+merchant_userid+",merchant_key="+merchant_key;
		}
		
		
		

	}
	
}
