package cn.finder.wx.request;

import java.util.Map;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.WeixinResponse;

/***
 * 客服接口 - 发送图片消息
 * @author Administrator
 *
 */
public class KFImageMsgSendRequest extends JsonStringRequest<WeixinResponse> {

	
	private String touser;
	
	private String msgtype="image";
	
	private ImageData image;
	
	
	
	public static class ImageData extends ApiObject{
		
		private String media_id;

		public String getMedia_id() {
			return media_id;
		}

		public void setMedia_id(String media_id) {
			this.media_id = media_id;
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

	public ImageData getImage() {
		return image;
	}

	public void setImage(ImageData image) {
		this.image = image;
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
