package cn.finder.wae.common.comm;

import org.apache.log4j.Logger;

import cn.xingkong.finder.jmail.FinderSendMail;
import cn.xingkong.finder.jmail.MailInfo;

@SuppressWarnings("all")
public class EmailUtil {
	private final static Logger logger=Logger.getLogger(EmailUtil.class);
	
	
	public static void sendEmail(String title,String fromEmail,String host,String toEmail,String userName,String password,String content){
		FinderSendMail sendMail;  
        MailInfo mailInfo =new MailInfo();  
        mailInfo.setTitle(title);  
//        mailInfo.setFrom("cnanubis@163.com");  
//        mailInfo.setHost("smtp.163.com");  
//        mailInfo.setTo("1017754092@qq.com");  
//        mailInfo.setUsername("cnanubis@163.com");  
//        mailInfo.setPassword("password$1");  
//        mailInfo.setContentType("text/html");  
//        mailInfo.setContent("<font color=\"red\">welcome to </font><a href=\"http://cnanubis.cn\">技术社区</a>"); 
        mailInfo.setFrom(fromEmail);  
        mailInfo.setHost(host);  
        mailInfo.setTo(toEmail);  
        mailInfo.setUsername(userName);  
        mailInfo.setPassword(password);  
        mailInfo.setContentType("text/html");  
        mailInfo.setContent(content);  
        sendMail=new FinderSendMail(mailInfo);  
        try {
			sendMail.send();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	public static void main(String[] args) {
		sendEmail("test","1017754092@qq.com","","","","","");
	}
}
