package cn.finder.wae.push.framework.client.test;

import org.ddpush.im.util.StringUtil;

import cn.finder.wae.push.framework.client.Message;
import cn.finder.wae.push.framework.client.WaeFinderUDPClient;

public class MyTest {

	static String pushSrvHost="iv.cwintop.com";
	static int pushSrvIp=9968;
	public static void main(String[] args){
		
		try{
			//String user=StringUtil.md5("yuzong"); //服务器端返回的直接是MD5格式
			String user="005";
			//登陆成功后 生成UUID
			MyWaeFinderUDPClient client=new MyWaeFinderUDPClient(user,1, pushSrvHost, pushSrvIp);
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
	
	
	static class MyWaeFinderUDPClient extends WaeFinderUDPClient{

		public MyWaeFinderUDPClient(String uuid, int appid, String serverAddr,
				int serverPort) throws Exception {
			super(uuid, appid, serverAddr, serverPort);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onPushMessage(Message message) {
			try{
				if(message!=null){
					System.out.println("===commandName:"+message.getCommandName());
					System.out.println("===commandValue size:"+message.getCommandValue().size());
					System.out.println("===title:"+message.getTitle());
					System.out.println("===content:"+message.getContent());
				}
			
			}catch(Exception exp){
				
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
