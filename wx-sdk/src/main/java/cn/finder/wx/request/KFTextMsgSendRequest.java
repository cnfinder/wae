package cn.finder.wx.request;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.WeixinResponse;

/***
 * 客服接口 - 发送文本消息
 * @author Administrator
 *
 */
public class KFTextMsgSendRequest extends JsonStringRequest<WeixinResponse> {

	
	private String touser;
	
	private String msgtype="text";
	
	private TextData text;
	
	
	
	
	public void setText(TextData text) {
		this.text = text;
	}

	public static class TextData extends ApiObject{
		
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
		
		
		
	}
	
	
	
	
	
	
	public TextData getText() {
		return text;
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
