package cn.finder.wx.domain.resp;

import java.util.Date;

public class ImgMsgSend implements MsgSendInterface{

	private String toUserName;
	
	private String fromUserName;
	private long createTime;
	
	private String msgType="image";
	
	
	private Image image;
	
	
	
	
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	

	public String toXml() {
		StringBuffer sb = new StringBuffer();  
        Date date = new Date(); 
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(toUserName).append("]]></ToUserName>");  
        sb.append("<FromUserName><![CDATA[").append(fromUserName).append("]]></FromUserName>");  
        sb.append("<CreateTime>").append(date.getTime()).append("</CreateTime>");  
        sb.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>"); 
        
        
        sb.append("<Image>");
        sb.append("<MediaId><![CDATA[").append(image.getMediaId()).append("]]></MediaId>");
        sb.append("</Image>");
        
        sb.append("</xml>");
        return sb.toString(); 
	}

	
	
	public static class Image{
		private String mediaId;

		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
		
		
	}
}
