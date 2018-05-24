package cn.finder.wae.global.api.request;

import cn.finder.httpcommons.request.SearchRequest;
import cn.finder.wae.global.api.response.MediaDescInfoResponse;

public class MediaDescInfoRequest extends SearchRequest<MediaDescInfoResponse> {

	@Override
	public String apiName() {
		return "wae.global.image.descfiles";
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
