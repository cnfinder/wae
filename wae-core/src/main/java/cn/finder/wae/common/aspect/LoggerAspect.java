package cn.finder.wae.common.aspect;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;


public class LoggerAspect {
	private final static Logger logger=Logger.getLogger(LoggerAspect.class);
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
	
			
		//这里可以通过异步写入数据库
		Date start = new Date();
		long startnano = System.nanoTime();
		String signature = joinPoint.getSignature().toLongString();
		
		
		/*HttpSession session  = AppContent.getSession();
		if(session!=null){
			User user = (User)session.getAttribute(Constant.KEY_SESSION_USER);
			//可以读取到
			logger.debug("=======user id:"+user.getAccount());
		}
		*/
		
		Object returnVal = joinPoint.proceed();
		
		

		logger.debug(start + " 执行 [" + signature + "] 共花费的时间是 "
				+ (System.nanoTime() - startnano) + " 纳秒");
		logger.debug("return obj:"+returnVal);
		
		new  LoggerDBThread(new Log()).start();
		
		return returnVal;
		
	}
	
	/***
	 * 异步写日志到数据库
	 * @author Administrator
	 *
	 */
	class LoggerDBThread extends Thread
	{
		private Log log;
		
		public LoggerDBThread(Log log)
		{
			this.log = log;
		}
		
		

		@Override
		public void run() {
			
			
		}
		
		
	}
	
	class Log
	{
		
	}
	
}
