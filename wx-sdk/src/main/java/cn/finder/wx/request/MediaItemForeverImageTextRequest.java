package cn.finder.wx.request;

import cn.finder.httpcommons.request.SearchRequest;
import cn.finder.wx.response.MediaItemForeverImageTextResponse;

/***
 * 素材 - 获取永久素材 图文消息
 * @author Administrator
 *
 */
public class MediaItemForeverImageTextRequest extends SearchRequest<MediaItemForeverImageTextResponse> {

	
	private String media_id;
	
	
	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
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
