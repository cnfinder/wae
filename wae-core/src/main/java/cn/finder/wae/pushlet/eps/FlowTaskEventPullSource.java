package cn.finder.wae.pushlet.eps;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventPullSource;
import nl.justobjects.pushlet.core.Session;
import nl.justobjects.pushlet.core.SessionManager;
import cn.finder.wae.pushlet.PushWebSender;

public class FlowTaskEventPullSource extends EventPullSource implements Serializable  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4832079402022176285L;

	protected long getSleepTime() {
		// TODO Auto-generated method stub
		return 3000;
	}

	protected Event pullEvent() {

		
		
		
		Session[] sessions= SessionManager.getInstance().getSessions();
		StringBuilder str = new StringBuilder(""); 
		if(sessions.length>0){
			
			
            for (int i = 0; i < sessions.length; i++) {  
                if (sessions[i].getId().equals("finder_unknown")) 
                	continue;  
                else
                	str.append(",").append(sessions[i].getId());   
            }  
            if(str.length()>0){
            	str.deleteCharAt(0);
            }
            
		}else{
			return null;
		}
		System.out.println(str);
		String msg="";
		try {
			msg=URLEncoder.encode("好 who?","utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Event event=Event.createDataEvent("/max");
		event.setField("min", msg + System.currentTimeMillis()
				);
		
		event.setField("userids", str.toString());*/
		
		PushWebSender sender=new PushWebSender();
		Map<String,Object> values=new HashMap<String, Object>();
		values.put("msg", msg);
		sender.sendToUser("/cs", "whl-test", values);
		//Dispatcher.getInstance().unicast(event,"dragon");
		
		return null;
	}
	 
}
