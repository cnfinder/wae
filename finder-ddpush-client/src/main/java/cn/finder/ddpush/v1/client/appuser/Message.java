package cn.finder.ddpush.v1.client.appuser;

import java.util.Map;

/***
 * 数据内容为JSON字符串
 * @author Administrator
 *
 */
public class Message {

	
	//private String fromUser; // 发送者用户的UUID
	
	//private String toUser;//本来就是当前用户
	
	private String content; //原始的json数据
	
	private Map<String,Object> data;
	
	private org.ddpush.im.v1.client.appuser.Message originalMessage;



	/***
	 * get from user uuid - 原生值
	 * @return
	 */
	public String getFromUser() {
		Object fromUser=getData("fromUser");
		if(fromUser!=null)
			return fromUser.toString();
		return null;
	}


	








	/*
	 * get to user uuid
	 */
	/*public String getToUser() {
		Object toUser=getData("toUser");
		if(toUser!=null)
			return toUser.toString();
		return null;
	}*/







	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public org.ddpush.im.v1.client.appuser.Message getOriginalMessage() {
		return originalMessage;
	}



	public void setOriginalMessage(
			org.ddpush.im.v1.client.appuser.Message originalMessage) {
		this.originalMessage = originalMessage;
	}



	public Map<String, Object> getData() {
		return data;
	}

	public Object getData(String key) {
		return data.get(key);
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
	
}
