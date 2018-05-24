package cn.finder.wae.controller.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.comet4j.core.CometConnection;
import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.listener.ConnectListener;

/***
 * 网页版推送 -基于Comet
 * @author Administrator
 *
 */
public class ArachCometServletContextListener implements ServletContextListener {
	private  Logger logger=Logger.getLogger(ArachCometServletContextListener.class);
	private List<String> channels=new ArrayList<String>();

	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		channels.add("default_channel");
		channels.add("flow_channel");
		channels.add("rreport_channel");//新的故障报修 通知
		
		
		CometContext cc = CometContext.getInstance();

		if (channels != null && channels.size() > 0) {

			for (String channel : channels) {
				cc.registChannel(channel);// 注册应用的channel
			}
		}
		CometEngine engine = cc.getEngine();
		engine.addConnectListener(new JoinConnectListener());
		
		/*Thread helloAppModule = new Thread(new HelloAppModule(),"Sender App Module");  
        helloAppModule.setDaemon(true);  
        
        // 启动线程  
        helloAppModule.start();*/

		
		
	}
	
	/***
	 *  加入连接执行监听处理
	 * @author whl
	 *
	 */
	class JoinConnectListener extends ConnectListener {

		@Override
        public boolean handleEvent(ConnectEvent anEvent) {
            CometConnection conn = anEvent.getConn();
            CometContext.getInstance().getEngine().sendTo(channels.get(0),conn,"欢迎上线");
            return true;
        }
       
	}

	
	class HelloAppModule implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				CometEngine engine = CometContext.getInstance().getEngine();

				for (String channel : channels) {
					engine.sendToAll(channel,Runtime.getRuntime().freeMemory() / 1024);
					//engine.sendTo(channel , engine.getConnection(someConnectionId),“Hi,我是XXX”);		
				}

			}
		}
	}
	
	
}
