package cn.finder.wx.request;

import java.util.Map;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.WeixinResponse;

/***
 * 客服接口 - 发送语音消息
 * @author Administrator
 *
 */
public class KFVoiceMsgSendRequest extends JsonStringRequest<WeixinResponse> {

	
	private String touser;
	
	private String msgtype="voice";
	
	private VoiceData voice;
	
	
	
	public static class VoiceData extends ApiObject{
		
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

	public VoiceData getVoice() {
		return voice;
	}

	public void setVoice(VoiceData voice) {
		this.voice = voice;
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
