package cn.finder.wae.controller.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.wae.business.domain.NotificationInfo;
import cn.finder.wae.business.module.notification.service.NotificationService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;

/**
 * 
 * @author: finder
 * @data:2014-4-8 2:24:10
 * @function: 从 消息队列表中 循环读取消息进行发送客户端
 * 暂时不用了
 */
@Deprecated
public class NIPushServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(NIPushServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847503911589250340L;


	public void init(ServletConfig servletConfig) throws ServletException {
		
		logger.debug("NIPushServlet.init  start....  ");
		
		ServletContext ctxt=servletConfig.getServletContext();
		
		String enabled = servletConfig.getInitParameter("enabled");
		if("true".equalsIgnoreCase(enabled)){
			logger.debug("NIPushServlet is enabled ....  ");
			
			try {
				//休眠  以便让缓存先加载好 
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new NIPushThread(ctxt).start();
		}
		
		logger.debug("NIPushServlet.init  end....  ");
		
	}

	
	static class NIPushThread extends Thread
	{
		private NotificationService notificationService;
		
		
		private  String apiKey = "";
		private  ServletContext ctxt;
		
		private long seconds;
		public NIPushThread(ServletContext ctxt)
		{
			this.ctxt = ctxt;
			notificationService =WebApplicationContextUtils.getWebApplicationContext(ctxt).getBean("notificationService",NotificationService.class);
			
			apiKey =Config.getString("apiKey", "");
			logger.debug("apiKey=" + apiKey);
			
			seconds =Long.parseLong(ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_NIPUSH_TIMER).getValue());
		}

		@Override
		public void run() {
			while(true)
			{
				try
				{
					
					//保存在线的用户名，使用在线的用户去数据库里面查找效率高， 因为数据在内存
					List<String> onlineUsers =new ArrayList<String>();
					
					Collection<ClientSession> clientSessions=SessionManager.getInstance().getSessions();
					for(ClientSession session:clientSessions)
					{
						if (session.getPresence().isAvailable()) {
							onlineUsers.add(session.getUsername());
			            }
					}
					
					logger.debug("===online session size:"+onlineUsers.size());
					if(onlineUsers.size()>0)
					{
						List<NotificationInfo> notifcations = notificationService.queryOnlineNotificationInfos(onlineUsers);
						if(notifcations!=null)
						{
							logger.debug("===will send notification  size:"+notifcations.size());
						}
						if(notifcations!=null && notifcations.size()>0)
						{
							for(NotificationInfo ni:notifcations)
							{
								logger.debug("send to "+ni.getToUser()+" msg:"+new JSONObject(ni).toString());
								new SendNotificationThread(apiKey, ni.getToUser(),ni.getTitle(),
										new JSONObject(ni).toString(),ni.getUrl()).start();
								
							}
						}
					}
					
					Thread.sleep(seconds*1000);
				}
				catch(Exception e)
				{
					logger.error(e.toString(), e);
				}
			}
		}
	}
	
	
	/***
	 * 发送消息线程
	 * @author finder
	 *
	 */
	static class SendNotificationThread extends Thread
	{
		private String apiKey;
		private String userName;
		private String title;
		private String msg;
		private String uri;
		
		
		private NotificationManager notificationManager = new NotificationManager();
		
		public SendNotificationThread(String apiKey, String userName,
				String title, String msg, String uri) {
			super();
			this.apiKey = apiKey;
			this.userName = userName;
			this.title = title;
			this.msg = msg;
			this.uri = uri;
		}

		@Override
		public void run() {
			notificationManager.sendNotifcationToUser(apiKey, userName, title, msg, uri);
		}
		
		
	}
}
