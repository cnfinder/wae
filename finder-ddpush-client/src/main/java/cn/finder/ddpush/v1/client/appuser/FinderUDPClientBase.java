package cn.finder.ddpush.v1.client.appuser;

import java.util.Map;

import org.ddpush.im.util.StringUtil;
import org.ddpush.im.v1.client.appuser.Message;
import org.ddpush.im.v1.client.appuser.UDPClientBase;

/***
 * json数据格式UDPClient
 * @author whl
 *
 */
public abstract class FinderUDPClientBase  extends UDPClientBase
{
	

	public FinderUDPClientBase(String uuid, int appid, String serverAddr,
			int serverPort) throws Exception {
		super(StringUtil.md5Byte(uuid), appid, serverAddr, serverPort);
	}


	public void onPushMessage(Message message) {
		cn.finder.ddpush.v1.client.appuser.Message msg=null;
		if(message==null || message.getData() == null || message.getData().length == 0){
			msg=null;
		}
		else{
			
			
			msg=new cn.finder.ddpush.v1.client.appuser.Message();
			msg.setOriginalMessage(message);
			
			
			if(message.getCmd() == Message.CMD_0x20){
				msg=new cn.finder.ddpush.v1.client.appuser.Message();
				//自定义消息类型
				String str = null;
				try{
					str = new String(message.getData(),5,message.getContentLength(), "UTF-8");
					
					msg.setContent(str);
					
				    Map<String,Object> mapData=	JsonUtils.getMap4Json(str);
					msg.setData(mapData);
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			else
			{
				throw new RuntimeException("===only self model supported!");
			}
		}
		onPushMessage(msg);
	}

	public abstract void onPushMessage(cn.finder.ddpush.v1.client.appuser.Message message);
	
}
