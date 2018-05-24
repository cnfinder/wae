package cn.finder.ddpush.v1.client.demo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.finder.ddpush.v1.client.appserver.FinderPusher;
import cn.finder.ddpush.v1.client.appuser.FinderUDPClientBase;
import cn.finder.ddpush.v1.client.appuser.Message;

public class FinderUDPClientDemo {
	static String pushSrvHost="iv.cwintop.com";
	static int pushSrvIp=9968;
	
	public static void main(String[] args) {
		
		try{
			String user="0000000000000000000000000000kjuh"; //服务器端返回的直接是MD5格式
			//登陆成功后 生成UUID
			MyFinderUDPClient client=new MyFinderUDPClient(user,1, pushSrvHost, pushSrvIp);
			client.setHeartbeatInterval(50);
			client.start();
			synchronized(client){
				client.wait();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	

	@Test
	public void testUDPPusher(){
		
		String user="qiaoy-test"; //服务器端返回的直接是MD5格式
		try {
			
			FinderPusher pusher=new FinderPusher(pushSrvHost, 9998, 1000*5,user);
			
			Map<String,Object> data=new HashMap<String,Object>();
			data.put("text", "积分asas");
			boolean result = pusher.push0x20MessageExt("0000000000000000000000000000kjuh", data);
			
			
			if(result){
				System.out.println("发送成功");
			}else{
				System.out.println("发送失败！格式有误");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static class MyFinderUDPClient extends FinderUDPClientBase{

		public MyFinderUDPClient(String uuid, int appid, String serverAddr,
				int serverPort) throws Exception {
			super(uuid, appid, serverAddr, serverPort);
		}

		@Override
		public void onPushMessage(Message message) {
			
			if(message!=null){
				String fromUser = message.getFromUser();
				String text=message.getData("text").toString();
				// toUser
				System.out.println("fromUser:"+fromUser+",text:"+text);
			}
		}

		@Override
		public boolean hasNetworkConnection() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void trySystemSleep() {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
}
