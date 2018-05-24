package cn.finder.wae.global.api.request;

import cn.finder.httpcommons.request.DefaultRequest;
import cn.finder.httpcommons.response.ApiResponse;

public class MediaDeleteRequest extends DefaultRequest<ApiResponse> {

	@Override
	public String apiName() {
		return "wae.global.image.delete";
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	
	private String media_id;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
	

}
