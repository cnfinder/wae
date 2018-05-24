package cn.finder.wae.controller.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class CustomedContextLoaderListener  implements ServletContextListener{

	public static ScheduledExecutorService executorService=null;
	
	public static  boolean runState=true; 
	
	private Logger logger=Logger.getLogger(CustomedContextLoaderListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		logger.info("===准备关闭executorService");
		
		//if(executorService!=null && !executorService.isShutdown()){
			try{
				executorService.shutdownNow();
			}
			catch(Throwable e){
				logger.error("===executorService.shutdownNow() error "+e);
			}finally{
				runState=false;
				logger.info("===关闭了executorService");
			}
			
			
		//}else{
			//logger.info("===不需要关闭executorService:"+executorService);
		//}
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		executorService=Executors.newScheduledThreadPool(20);
	}

	
	
	public synchronized static ScheduledExecutorService getExecutorService(){
		return CustomedContextLoaderListener.executorService;
	}

	public synchronized static boolean getRunState(){
		if(executorService==null ||executorService.isShutdown() ||executorService.isTerminated() || executorService.toString().indexOf("Running")==-1){
			runState=false;
		}
		return runState;
	}
	
}
