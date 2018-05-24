package cn.finder.wae.common.push;

import java.util.Map;

import org.apache.log4j.Logger;

import cn.finder.ddpush.v1.client.appserver.FinderPusher;

public class Pusher {
	
	private Logger logger=Logger.getLogger(Pusher.class);

	//推送服务器主机
	public  static String host;
	
	//服务器的推送端口
	public  static  int pushPort;
	
	
	//服务推送用户的sessionKey
	public  static String sessionKey;
	
	
	//超时时间
	public static int timeoutMillis;
	
	
	/***
	 * 发送推送信息
	 * @param toUser
	 * @param data
	 */
	public boolean doPush(String toUser,Map<String,Object> data){
		
		try{
			FinderPusher finderPusher=new FinderPusher(host,pushPort,timeoutMillis,sessionKey);
			
			boolean bRet=finderPusher.push0x20MessageExt(toUser, data);
			return bRet;
		}
		catch(Exception e){
			logger.equals(e);
			return false;
		}
		
		
	}
	
	/***
	 * 异步发送推送消息
	 * @param toUser
	 * @param data
	 */
	public void doPushInThread(final String toUser,final Map<String,Object> data){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				
				doPush(toUser,data);
				
			}
			
		}).start();
	}
	
	
	
	
	
	
	public  void setHost(String host){
		Pusher.host=host;
	}
	
	public  void setPushPort(int pushPort){
		Pusher.pushPort=pushPort;
	}
	
	public  void setSessionKey(String sessionKey){
		Pusher.sessionKey=sessionKey;
	}

	public  void setTimeoutMillis(int timeoutMillis) {
		Pusher.timeoutMillis = timeoutMillis;
	}

	
	
}
