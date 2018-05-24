package cn.finder.wx.request;

import cn.finder.httpcommons.request.XmlStringRequest;
import cn.finder.wx.response.WxUnifiedorderResponse;

/***
 * 微信付款 请求
 * @author whl
 *
 */
public class WxUnifiedorderRequest extends XmlStringRequest<WxUnifiedorderResponse>{

	
	private String appid;
	
	private String body;
	
	private String mch_id;
	
	private String nonce_str;
	
	private String notify_url;
	
	private String out_trade_no;
	
	private int total_fee;
	
	private String trade_type;
	
	private String openid;
	
	private String attach;
	
	private String spbill_create_ip;
	
	private String device_info;
	
	private String sign;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
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
	

	@Override
	public String toString() {
		return "WxUnifiedorderRequest [appid=" + appid + ", body=" + body + ", mch_id=" + mch_id + ", nonce_str="
				+ nonce_str + ", notify_url=" + notify_url + ", out_trade_no=" + out_trade_no + ", total_fee="
				+ total_fee + ", trade_type=" + trade_type + ", openid=" + openid + ", attach=" + attach
				+ ", spbill_create_ip=" + spbill_create_ip + ", device_info=" + device_info + ", sign=" + sign + "]";
	}

	@Override
	public String apiName() {
		
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
}
