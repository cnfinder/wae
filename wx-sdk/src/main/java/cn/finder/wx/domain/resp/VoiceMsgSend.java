package cn.finder.wx.domain.resp;

import java.util.Date;


/***
 * 返回语音消息
 * @author whl
 *
 */
public class VoiceMsgSend implements MsgSendInterface{

	private String toUserName;
	
	private String fromUserName;
	
	private long createTime;

	private String msgType="voice";
	
	private Voice voice;
	
	

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	
	
	
	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}

	public String toXml() {
		StringBuffer sb = new StringBuffer();  
        Date date = new Date(); 
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(toUserName).append("]]></ToUserName>");  
        sb.append("<FromUserName><![CDATA[").append(fromUserName).append("]]></FromUserName>");  
        sb.append("<CreateTime>").append(date.getTime()).append("</CreateTime>");  
        sb.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>"); 
        
        sb.append("<Voice>");
    	
    	sb.append("<MediaId><![CDATA[").append(voice.getMediaId()).append("]]></MediaId>");
    	
        sb.append("</Voice>");
        
       
        sb.append("</xml>");
        return sb.toString();  
	}
	
	public static class Voice{
		private String mediaId;

		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
		
		
		
	}
	
	
}
