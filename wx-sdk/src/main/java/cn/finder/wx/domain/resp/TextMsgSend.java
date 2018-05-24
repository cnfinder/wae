package cn.finder.wx.domain.resp;

import java.util.Date;


/***
 * 封装文字类的返回消息
 * @author whl
 *
 */
public class TextMsgSend implements MsgSendInterface{

	private String toUserName;
	
	private String fromUserName;
	
	private String content;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String toXml() {
		StringBuffer sb = new StringBuffer();  
        Date date = new Date();  
        sb.append("<xml><ToUserName><![CDATA[");  
        sb.append(toUserName);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");  
        sb.append(fromUserName);
        sb.append("]]></FromUserName><CreateTime>");  
        sb.append(date.getTime());  
        sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");  
        sb.append(content);  
        sb.append("]]></Content><FuncFlag>0</FuncFlag></xml>");  
        return sb.toString();  
	}
	
	
	
}
