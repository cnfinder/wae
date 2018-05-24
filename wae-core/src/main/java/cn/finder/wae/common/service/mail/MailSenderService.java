package cn.finder.wae.common.service.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.thread.AppContent;

public class MailSenderService {

	private JavaMailSenderImpl mailSender;
	
//	private SimpleMailMessage templateMessage;
	
	public void setMailSender(JavaMailSenderImpl mailSender)
	{
		this.mailSender=mailSender;
	}
	/*
	public void setTemplateMessage(SimpleMailMessage templateMessage)
	{
		this.templateMessage=templateMessage;
	}*/
	
	
	
	private static MailSenderService instance=null;
	public static MailSenderService getInstance(ServletContext servletContext)
	{
		if(instance==null)
		{
			//synchronized (instance) {
				if(instance==null)
				{
				
					
					instance=AppApplicationContextUtil.getContext().getBean("mailSenderService", MailSenderService.class);
				}
			//}
		}
		return instance;
	}
	
	public void send(String from,String[] to,String[] cc,String[] bcc,String subject,String text,
			String[] inlinpath,String[] attach) throws MessagingException
	{
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper senderHelper=new MimeMessageHelper(message, true);
		
		if(!StringUtils.isEmpty(from)){
			senderHelper.setFrom(from);
		}
		if(to!=null && to.length>0)
		{
			senderHelper.setTo(to);
		}
		if(cc!=null && cc.length>0)
		{
			senderHelper.setCc(cc);
		}
		if(bcc!=null && bcc.length>0)
		{
			senderHelper.setBcc(bcc);
		}
		senderHelper.setSubject(subject);
		senderHelper.setText(text,true);
		if(inlinpath!=null && inlinpath.length>0)
		{
			for(String p:inlinpath)
			{
				File f=new File(p);
				FileSystemResource res=new FileSystemResource(f);
				senderHelper.addInline(f.getName()+System.nanoTime(), res);
			}
		}
		if(attach!=null && attach.length>0)
		{
			for(String p:attach)
			{
				File f=new File(p);
				FileSystemResource res=new FileSystemResource(f);
				senderHelper.addAttachment(f.getName(), res);
			}
		}

		mailSender.send(message);
		
	}
}
