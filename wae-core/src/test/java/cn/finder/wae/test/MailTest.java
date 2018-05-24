package cn.finder.wae.test;

import org.junit.Test;

import cn.xingkong.finder.jmail.FinderSendMail;
import cn.xingkong.finder.jmail.MailInfo;

public class MailTest {

	
	@Test
	public void testMailSend() throws Exception{
		FinderSendMail sendMail=null;  
        MailInfo mailInfo =new MailInfo();  
        mailInfo.setTitle("测试啦");  
        mailInfo.setFrom("cnxingkong@163.com");  
        mailInfo.setHost("smtp.163.com");  
        mailInfo.setTo("171721626@qq.com");  
        mailInfo.setUsername("cnxingkong@163.com");  
        mailInfo.setPassword("password$1");  
        mailInfo.setContentType("text/html");  
        mailInfo.setContent("sfjoajdfjasdfa");  
        sendMail=new FinderSendMail(mailInfo);  

		sendMail.send();
	}
}
