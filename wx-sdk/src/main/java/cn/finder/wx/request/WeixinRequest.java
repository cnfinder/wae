package cn.finder.wx.request;

import cn.finder.httpcommons.request.DefaultRequest;
import cn.finder.wx.response.WeixinResponse;

public   class WeixinRequest<T extends WeixinResponse> extends DefaultRequest<T> {

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
