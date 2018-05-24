package cn.finder.wae.common.comm.templatemsg;

import java.util.HashMap;
import java.util.Map;

import org.comet4j.core.CometContext;

import cn.finder.common.util.JsonUtil;
import cn.finder.wae.business.domain.TemplateMsg;

public class WebTemplateMsgSender {

	private TemplateMsg tm;
	
	public WebTemplateMsgSender(TemplateMsg tm){
		this.tm=tm;
	}
	
	public  void send(final Map<String,Object> variables){
		 Object commandName=variables.get("commandName");
		    if(commandName==null || commandName.toString().trim()==""){
		    	commandName="wae_default_showTipEvent";
		    	
		    }
		    
		    
		    Object commandValue=variables.get("commandValue");
		    Object commandValue_parser = null;
		    if(commandValue==null){
		    	commandValue_parser="{}";
		    }else{
		    	commandValue_parser= new TemplateMsgParser() {
					@Override
					public Map<String, Object> findMapPara() {
						return variables;
					}
		    	}.parser(commandValue.toString());
		    }
		    
		    
		    
		    Object templateMsgCode=variables.get("templateMsgCode");
		    if(templateMsgCode==null){
		    	return ;
		    }
		    
		    
		    Object toUserId=variables.get("toUserId");
		    if(toUserId==null){
		    	toUserId="";
		    }
		    
		    Object pushChannel=variables.get("pushChannel");
		    if(pushChannel==null){
		    	if(pushChannel==null||pushChannel.toString().trim()==""){
		    		pushChannel="default_channel";
		    	}
		    }
		    
		    
		    //实际内容
		    String content= new TemplateMsgParser() {
				
				@Override
				public Map<String, Object> findMapPara() {
					return variables;
				}
			}.parser(tm.getTemplateContent());
			
			
			
			Map<String,Object> data=new HashMap<String, Object>();
			
			data.put("title",tm.getTitle());
			data.put("content", content);
			
			data.put("commandName",commandName);
			data.put("commandValue", commandValue_parser);
			
			String jsonData = JsonUtil.getJsonString4JavaPOJO(data);
			
			CometContext.getInstance().getEngine().sendToAll(pushChannel.toString(),jsonData);
	}
}
