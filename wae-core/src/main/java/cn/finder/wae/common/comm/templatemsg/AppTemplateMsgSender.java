package cn.finder.wae.common.comm.templatemsg;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.common.comm.EncoderHandler;
import cn.finder.wae.common.push.Pusher;

/***
 * DDPUSH APP推送
 * @author whl
 *
 */
public class AppTemplateMsgSender {

	
	private static Logger  logger=Logger.getLogger(AppTemplateMsgSender.class);
	
	private TemplateMsg tm;
	
	public AppTemplateMsgSender(TemplateMsg tm){
		this.tm=tm;
	}
	
	public  void send(final Map<String,Object> variables){
		 Object commandName=variables.get("commandName");
		    if(commandName==null){
		    	commandName="";
		    }
		    
		    
		    Object commandValue=variables.get("commandValue");
		    if(commandValue==null){
		    	commandValue="";
		    }
		    
		    
		    Object templateMsgCode=variables.get("templateMsgCode");
		    if(templateMsgCode==null){
		    	throw new RuntimeException("can't find parameter:templateMsgCode");
		    }
		    
		    
		    Object toUserId=variables.get("toUserId");
		    if(toUserId==null){
		    	throw new RuntimeException("can't find parameter:toUserId");
		    }
		    
		    String[] toUserIds = toUserId.toString().split(",");
		    
		    //实际内容
		    String content= new TemplateMsgParser() {
				
				@Override
				public Map<String, Object> findMapPara() {
					// TODO Auto-generated method stub
					return variables;
				}
			}.parser(tm.getTemplateContent());
			
			
			
			Map<String,Object> data=new HashMap<String, Object>();
			
			data.put("title",tm.getTitle());
			data.put("content", content);
			
			data.put("commandName",commandName);
			data.put("commandValue", commandValue);
			
			//发送推送消息
			Pusher pusher=new Pusher();
			//pusher.doPushInThread(toUserId.toString(), data);
			logger.info("=============APP推送开始==============");
			for(int i=0;i<toUserIds.length;i++){
				logger.info("=============用户id:"+toUserIds[i]+"==============");
				pusher.doPushInThread(EncoderHandler.encodeByMD5(toUserIds[i]), data);
				
				
			}
	}
}
