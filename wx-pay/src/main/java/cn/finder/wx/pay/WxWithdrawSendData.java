package cn.finder.wx.pay;

/**
 * 微信发送数据对象
 *
 */
public class WxWithdrawSendData 
{
	private String mch_appid;
	
	
	private String mchid;
	
	private String nonce_str;
	
	private String openid;
	
	
	private String spbill_create_ip;
	
	private String device_info;
	
	private String sign;
	
	private String partner_trade_no; //商户订单号
	
	/*
	 * 
	 * NO_CHECK：不校验真实姓名 
FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账） 
OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
	 */
	private String check_name;//校验用户姓名选项
	
	
	private String re_user_name;
	
	private int amount;
	
	private String desc; //企业付款操作说明信息。必填。
	
	
	public String getMch_appid() {
		return mch_appid;
	}



	public void setMch_appid(String mch_appid) {
		this.mch_appid = mch_appid;
	}




	public String getMchid() {
		return mchid;
	}



	public void setMchid(String mchid) {
		this.mchid = mchid;
	}



	public String getNonce_str() {
		return nonce_str;
	}


	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}





	public String getOpenid() {
		return openid;
	}



	public void setOpenid(String openid) {
		this.openid = openid;
	}

	

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}



	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getDevice_info() {
		return device_info;
	}


	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPartner_trade_no() {
		return partner_trade_no;
	}









	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}

	public String getCheck_name() {
		return check_name;
	}


	public void setCheck_name(String check_name) {
		this.check_name = check_name;
	}


	public String getRe_user_name() {
		return re_user_name;
	}

	public void setRe_user_name(String re_user_name) {
		this.re_user_name = re_user_name;
	}



	public int getAmount() {
		return amount;
	}



	public void setAmount(int amount) {
		this.amount = amount;
	}




	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "appid="+mch_appid+",mch_id="+mchid+",nonce_str="+nonce_str+",openid="+openid+",spbill_create_ip="+spbill_create_ip+",device_info="+device_info+",sign="+sign;
	}

	
	
	
	
	
	
	
	
}
