package cn.finder.wx.request;

import cn.finder.httpcommons.request.SearchRequest;
import cn.finder.wx.response.MediaListResponse;

/***
 * 素材 - 列表接口
 * @author Administrator
 *
 */
public class MediaListRequest extends SearchRequest<MediaListResponse> {

	
	public final static String TYPE_IMAGE="image";
	public final static String TYPE_VIDEO="video";
	public final static String TYPE_VOICE="voice";
	public final static String TYPE_NEWS="news"; //图文
	
	
	
	private String type;
	
	private int offset;
	
	private int count;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
