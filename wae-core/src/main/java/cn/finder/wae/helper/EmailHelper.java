package cn.finder.wae.helper;

import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.xingkong.finder.jmail.FinderSendMail;
import cn.xingkong.finder.jmail.MailInfo;

/***
 * 邮件辅助类
 * @author Administrator
 *
 */
public class EmailHelper {

	/***
	 * 项目邮件发送辅助
	 * @param title
	 * @param body
	 * @param mailTo
	 * @throws Exception
	 */
	public void sendEmail(String title,String body,String mailTo) throws Exception
	{
		SysConfigCache sysConfigCache = ArchCache.getInstance().getSysConfigCache();
		
		FinderSendMail sendMail=null;  
        MailInfo mailInfo =new MailInfo();  
        mailInfo.setTitle(title);  
        mailInfo.setFrom(sysConfigCache.get(SysConfigCache.KEY_SYSCONFIG_CONFIG_MAIL_USERNAME).getValue());  
        mailInfo.setHost(sysConfigCache.get(SysConfigCache.KEY_SYSCONFIG_CONFIG_MAIL_HOST).getValue());  
        mailInfo.setTo(mailTo);  
        mailInfo.setUsername(sysConfigCache.get(SysConfigCache.KEY_SYSCONFIG_CONFIG_MAIL_USERNAME).getValue());  
        mailInfo.setPassword(sysConfigCache.get(SysConfigCache.KEY_SYSCONFIG_CONFIG_MAIL_USERPWD).getValue());  
        mailInfo.setContentType("text/html");  
        mailInfo.setContent(body);  
        sendMail=new FinderSendMail(mailInfo);  
		sendMail.send();
	}
	
	
	/**
	 * 发送给管理员自己
	 * @param title
	 * @param body
	 */
	public void sendEmailToAdminInThread(String title,String body)
	{
		new SendMailThread(title, body, ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_MAIL_USERNAME).getValue()).start();
	}
	
	
	
	
	/***
	 * 项目邮件发送辅助
	 * @param title
	 * @param body
	 * @param mailTo
	 * @throws Exception
	 */
	public void sendEmailInThread(String title,String body,String mailTo) throws Exception
	{
		new SendMailThread(title, body, mailTo).start();
	}
	
	
	class SendMailThread extends Thread
	{
		String title;
		String body;
		String mailTo;
		
		
		public SendMailThread(String title, String body, String mailTo) {
			super();
			this.title = title;
			this.body = body;
			this.mailTo = mailTo;
		}


		@Override
		public void run() {
			
			try {
				sendEmail(title, body, mailTo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
}