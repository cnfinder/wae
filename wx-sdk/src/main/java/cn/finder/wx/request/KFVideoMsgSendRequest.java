package cn.finder.wx.request;

import java.util.Map;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.WeixinResponse;

/***
 * 客服接口 - 发送视频消息
 * @author Administrator
 *
 */
public class KFVideoMsgSendRequest extends JsonStringRequest<WeixinResponse> {

	
	private String touser;
	
	private String msgtype="video";
	
	private VideoData video;
	
	
	
	public static class VideoData extends ApiObject{
		
		private String media_id;
		
		private String thumb_media_id;
		
		private String title;
		
		
		private String description;


		public String getMedia_id() {
			return media_id;
		}


		public void setMedia_id(String media_id) {
			this.media_id = media_id;
		}


		public String getThumb_media_id() {
			return thumb_media_id;
		}


		public void setThumb_media_id(String thumb_media_id) {
			this.thumb_media_id = thumb_media_id;
		}


		public String getTitle() {
			return title;
		}


		public void setTitle(String title) {
			this.title = title;
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public VideoData getVideo() {
		return video;
	}

	public void setVideo(VideoData video) {
		this.video = video;
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
