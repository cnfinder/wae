package cn.finder.wx.request;



/***
 * 主动消息---图片消息
 * @author whl
 *
 */
public class ActiveImageMsgSendRequest extends ActiveMsgSendRequest{

	private String mediaId;
	
	public ActiveImageMsgSendRequest(String mediaId){
		
		this.msgtype="image";
		this.mediaId=mediaId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	

	
	
	
	
}
