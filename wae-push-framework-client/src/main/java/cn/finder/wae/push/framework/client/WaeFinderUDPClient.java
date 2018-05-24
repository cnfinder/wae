package cn.finder.wae.push.framework.client;

import java.util.Map;

import cn.finder.ddpush.v1.client.appuser.FinderUDPClientBase;
import cn.finder.ddpush.v1.client.appuser.JsonUtils;
import cn.finder.ddpush.v1.client.appuser.Message;

public abstract class WaeFinderUDPClient  extends FinderUDPClientBase{

	public WaeFinderUDPClient(String uuid, int appid, String serverAddr,
			int serverPort) throws Exception {
		super(uuid, appid, serverAddr, serverPort);
	}

	public void onPushMessage(Message message) {
		cn.finder.wae.push.framework.client.Message msg=null;
		if(message!=null){
			msg=new cn.finder.wae.push.framework.client.Message();
			
			msg.setOriginalMessage(message);
			
			String title=message.getData("title").toString();
			String content=message.getData("content").toString();
			String commandName=message.getData("commandName").toString();
			String commandValueStr=message.getData("commandValue").toString();
			
			Map<String,Object> commandValue=JsonUtils.getMap4Json(commandValueStr);
			if(commandValue!=null)
				msg.setCommandValue(commandValue);
			
			msg.setTitle(title);
			msg.setContent(content);
			msg.setCommandName(commandName);
			msg.setCommandValue(commandValue);
			
		}
		
		onPushMessage(msg);
		
	}

	public abstract void onPushMessage(cn.finder.wae.push.framework.client.Message message);
}
