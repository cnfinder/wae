package cn.finder.wae.common.service.mail;

import org.springframework.mail.MailSender;

import org.springframework.mail.SimpleMailMessage;


public class ExceptionMailImpl implements ExceptionMail{

	private MailSender mailSender;
	
	private SimpleMailMessage templateMessage;
	
	
	public void setMailSender(MailSender mailSender)
	{
		this.mailSender=mailSender;
		
	}
	public void setTemplateMessage(SimpleMailMessage templateMessage)
	{
		
		this.templateMessage=templateMessage;
	}
	
	@Override
	public void send(Throwable e) {
		// TODO Auto-generated method stub
		SimpleMailMessage msg=new SimpleMailMessage(templateMessage);
		msg.setTo("wuhualong13142008@163.com");
		msg.setText("test spring mail "+e.toString());
		mailSender.send(msg);
	}
	
}
