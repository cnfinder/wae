package cn.finder.wx.request;

import java.util.HashMap;
import java.util.Map;


/***
 * 主动消息---普通文本消息
 * @author whl
 *
 */
public class ActiveTextMsgSendRequest extends ActiveMsgSendRequest{

	
	public ActiveTextMsgSendRequest(String content){
		
		this.msgtype="text";
		this.text.put("content", content);
	}
	
	private Map<String,String> text=new HashMap<String, String>();

	public Map<String, String> getText() {
		return text;
	}

	public void setText(Map<String, String> text) {
		this.text = text;
	}
	
	
	
	
}
