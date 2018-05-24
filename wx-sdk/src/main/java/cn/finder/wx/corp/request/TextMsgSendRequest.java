package cn.finder.wx.corp.request;


/***
 * 发送文本消息
 * @author whl
 *
 */
public class TextMsgSendRequest extends MsgSendBaseRequest {

	
	private String msgtype="text";
	
	
	private String safe="0";
	
	private Text text;



	public String getMsgtype() {
		return msgtype;
	}



	public String getSafe() {
		return safe;
	}







	public void setSafe(String safe) {
		this.safe = safe;
	}







	public Text getText() {
		return text;
	}



	public void setText(Text text) {
		this.text = text;
	}



	public static class Text{
		private String content="";

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
	}



	
}
