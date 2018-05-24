package cn.finder.wx.response;

import cn.finder.httpcommons.response.ApiResponse;
/***微信 响应 基类
 * 
 * @author whl
 *
 */
public class WeixinResponse extends ApiResponse {

	protected String errcode;

	protected String errmsg;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	public boolean isSuccess(){
		if("0".equals(errcode))
			return true;
		return false;
	}
	
}
