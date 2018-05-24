package cn.finder.wae.pushlet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;


import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.SessionManager;

/***
 * 通过pushlet进行WEB消息发送
 * @author dragon
 *注意点  当在js中调用初始化的时候 千万不能出现异常，否则收不到消息
 *# time server should wait on refresing pull client  10min
pull.refresh.timeout.millis=600000 表示不工作时  保留回话时间  这个设置 10分钟
当相同用户上线后 随机分配到用户 ， 因为这个是根据心跳链接上 就发给谁
 */
public class PushWebSender {

	private static org.apache.log4j.Logger logger=Logger.getLogger(PushWebSender.class);
	
	/***
	 * 发送消息给用户
	 * @param userId
	 * @param values
	 */
	public boolean sendToUser(String subject,String userId,Map<String,Object> values){
		
		boolean hasSession=SessionManager.getInstance().hasSession(userId);
		logger.info("===hasSession:"+userId+" "+hasSession);
		if(hasSession){
			Event event=createEvent(subject,values);
			if(event!=null)
				Dispatcher.getInstance().unicast(event,userId);
			
		}
		return hasSession;
	}
	
	
	
	/***
	 * 向所有和subject名称匹配的事件推送
	 * @param subject
	 * @param values
	 */
	public void multicastToUsers(String subject,Map<String,Object> values){
		
		int sessionCnt=SessionManager.getInstance().getSessionCount();
		
		if(sessionCnt>0){
			Event event=createEvent(subject,values);
			if(event!=null)
				Dispatcher.getInstance().multicast(event);
		}
	}
	
	/***
	 * 向所有的事件推送，不要求和这儿的myevent1名称匹配  
	 * @param subject
	 * @param values
	 */
	public void broadcastToUsers(String subject,Map<String,Object> values){
		
		int sessionCnt=SessionManager.getInstance().getSessionCount();
		
		if(sessionCnt>0){
			Event event=createEvent(subject,values);
			if(event!=null)
				Dispatcher.getInstance().broadcast(event);
		}
	}
	
	
	
	/***
	 * 创建Event
	 * @param subject
	 * @param values
	 * @return
	 */
	private Event createEvent(String subject,Map<String,Object> values){
		Event event=Event.createDataEvent(subject);
		
		if(values!=null&&values.size()>0){
		   Set<Entry<String,Object>> entrySet=values.entrySet();
		   
		   Iterator<Map.Entry<String,Object>> iter=  entrySet.iterator();
		   
		   while(iter.hasNext()){
			   Entry<String,Object> entry= iter.next();
			   
			   String key="";
			   String value="";
			try {
				key = URLEncoder.encode(entry.getKey(),"UTF-8");
				value = URLEncoder.encode(entry.getValue()==null?"":entry.getValue().toString(),"UTF-8");
				
				event.setField(key, value);
			} catch (UnsupportedEncodingException e) {
				
				event=null;
			}
			   
		   }
			
		}
		return event;
	}
	
	
}
