package cn.finder.wae.push.framework.client;

import java.util.HashMap;
import java.util.Map;

public class Message {

	private String title;
	
	private String content;
	
	private String commandName;
	
	private Map<String,Object> commandValue=new HashMap<String, Object>();
	
	private cn.finder.ddpush.v1.client.appuser.Message originalMessage;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public Map<String, Object> getCommandValue() {
		return commandValue;
	}

	public void setCommandValue(Map<String, Object> commandValue) {
		if(commandValue!=null)
			this.commandValue = commandValue;
	}

	public cn.finder.ddpush.v1.client.appuser.Message getOriginalMessage() {
		return originalMessage;
	}

	public void setOriginalMessage(
			cn.finder.ddpush.v1.client.appuser.Message originalMessage) {
		this.originalMessage = originalMessage;
	}
	
	
	
}
