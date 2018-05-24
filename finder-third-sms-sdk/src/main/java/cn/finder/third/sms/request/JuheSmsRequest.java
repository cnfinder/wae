package cn.finder.third.sms.request;

import cn.finder.httpcommons.request.DefaultRequest;
import cn.finder.third.sms.response.JuheSmsResponse;

/***
 * 聚合
 * @author whl
 *
 */
public class JuheSmsRequest extends DefaultRequest<JuheSmsResponse>{

	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	
	private String mobile="";
	private String tpl_id="";
	private String tpl_value="";
	private String key="";
	private String dtype="json";
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTpl_id() {
		return tpl_id;
	}

	public void setTpl_id(String tpl_id) {
		this.tpl_id = tpl_id;
	}

	public String getTpl_value() {
		return tpl_value;
	}

	public void setTpl_value(String tpl_value) {
		this.tpl_value = tpl_value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	
	
	
	
	
	
	
}
