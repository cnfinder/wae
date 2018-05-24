package cn.finder.wx.request;

import java.util.List;
import java.util.Map;

import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.WeixinResponse;

public class WXMenuRequest extends JsonStringRequest<WeixinResponse> {

	private List<Map<String,Object>> button;
	
	
	
	
	
	public void setMenus(List<Map<String, Object>> button) {
		this.button = button;
	}




	public List<Map<String, Object>> getButton() {
		return button;
	}

	public void setButton(List<Map<String, Object>> button) {
		this.button = button;
	}

	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	
	

}
