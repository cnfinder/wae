package cn.finder.wae.controller.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.http.HttpUtil;

/***
 * 解决pushlet先要 通过网页 初始化的BUG  
 * 当在系统启动后 ，就默认网页发送get 请求初始化
 * 
 * http://localhost/mk/pushlet.srv?p_event=join-listen&p_format=xml-strict&p_mode=pull&userid=&p_subject=/im
 * @author finder
 *
 */
public class PushletInitServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(PushletInitServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847503911589250340L;


	public void init(ServletConfig servletConfig) throws ServletException {
		
		
		
		
		String enabled = servletConfig.getInitParameter("enabled");
		String subjects=servletConfig.getInitParameter("subject");
		String context=servletConfig.getInitParameter("context");
		logger.info("====="+subjects);
		if("true".equalsIgnoreCase(enabled)){
			
			if(!StringUtils.isEmpty(subjects)){
				try {
					Thread.sleep(5000);
					new PushletInitThread(subjects,context).start();
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	         
		}
		
	}
	
	
	
	public static class PushletInitThread extends Thread{
		String subjects;
		String context;
		public PushletInitThread(String subjects,String context){
			this.subjects=subjects;
			this.context=context;
			
		}
		@Override
		public void run() {
			String[] subjectArr=subjects.split(",");
			
			for(int i=0;i<subjectArr.length;i++){
				String subject=subjectArr[i];
				try{
					
					logger.info("======subject");
					String url=context+"/pushlet.srv?p_event=join-listen&p_format=xml-strict&p_mode=pull&userid=&p_subject="+subject;
					//一定要在线程里面执行 ，否则不可预知的错误
					 String retStr=HttpUtil.get(url);
					 if(retStr.indexOf("join-listen-ack")!=-1){
						 logger.info("====主题:"+subject+" 订阅成功");
					 }
					 
				}
				catch(Exception e){
					logger.error("===主题："+subject+"订阅失败 "+e);
				}
				finally{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		
		
		
	}
	

}
