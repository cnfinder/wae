package cn.finder.wx.request;

import cn.finder.httpcommons.request.UploadRequest;
import cn.finder.wx.response.WeixinResponse;

public abstract  class WeixinUploadRequest<T extends WeixinResponse> extends UploadRequest<T> {

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
