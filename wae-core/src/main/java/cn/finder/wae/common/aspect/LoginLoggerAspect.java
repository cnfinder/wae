package cn.finder.wae.common.aspect;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.exception.WaeRuntimeException;


public class LoginLoggerAspect {
	private final static Logger logger=Logger.getLogger(LoginLoggerAspect.class);
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		try {
			
			//这里可以通过异步写入数据库
			Date time = new Date();
			
			Object[] args = joinPoint.getArgs();
			User userArg = (User)args[0];
			
			String uname = userArg.getAccount();
			String pwd = userArg.getPassword();
			
			int status=0;
			Object returnVal = joinPoint.proceed();
			
			if(returnVal==null){
				//登录失败，一般是账户输入错误
				logger.debug("== 登录失败");
				status = 0;
			}else{
				pwd="";
				status=1;
			}
			
			
			
			logger.debug("== uname:"+uname+", pwd:"+pwd);

			
			new  LoggerDBThread(new Log(time,uname,pwd,1,"",status)).start();
			
			return returnVal;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new WaeRuntimeException(e);
		}
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
	
	
	class LoggerJdbc extends BaseJdbcDaoSupport{
		
		public void addLog(){
			
			String sql="insert into ";
		}
	}
	
	class Log
	{
		
		public Log(Date logDate, String userName, String password, int logType,
				String ip, int status) {
			super();
			this.logDate = logDate;
			this.userName = userName;
			this.password = password;
			this.logType = logType;//1登陆  0 登出
			this.ip = ip;
			this.status = status;
		}


		private Date logDate;
		
		private String userName;
		private String password;
		
		//1 - 登录日志 
		private int logType;
		
		
		private String ip;
		
		
		//0 - 登录失败  1-登录成功
		private int status;


		public Date getLogDate() {
			return logDate;
		}


		public void setLogDate(Date logDate) {
			this.logDate = logDate;
		}


		public String getUserName() {
			return userName;
		}


		public void setUserName(String userName) {
			this.userName = userName;
		}


		public String getPassword() {
			return password;
		}


		public void setPassword(String password) {
			this.password = password;
		}


		public int getLogType() {
			return logType;
		}


		public void setLogType(int logType) {
			this.logType = logType;
		}


		public String getIp() {
			return ip;
		}


		public void setIp(String ip) {
			this.ip = ip;
		}


		public int getStatus() {
			return status;
		}


		public void setStatus(int status) {
			this.status = status;
		}
		
		
		
		
	}
	
}
