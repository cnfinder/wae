package cn.finder.wae.business.domain;

import java.util.Date;
/***
 * 登陆信息实体类
 * @author Xuchao
 *
 */
public class Log implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3612297074887943467L;
	
	private int id;
	private int user_id;
	private Date exit_time;
	private Date logDate;
	private String levelName;
	private String lineNumber;
	private String methodName;
	private String className;
	private String threadName;
	private String message;
	private String iP;
	private String userName;
	private int logtype;
	private String sessionId;
	
	
	public Log() {
		super();
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getUser_id() {
		return user_id;
	}



	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}



	public Date getExit_time() {
		return exit_time;
	}



	public void setExit_time(Date exit_time) {
		this.exit_time = exit_time;
	}



	public Date getLogDate() {
		return logDate;
	}



	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}



	public String getLevelName() {
		return levelName;
	}



	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}



	public String getLineNumber() {
		return lineNumber;
	}



	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}



	public String getMethodName() {
		return methodName;
	}



	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public String getThreadName() {
		return threadName;
	}



	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getiP() {
		return iP;
	}



	public void setiP(String iP) {
		this.iP = iP;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public void setiP(byte[] address) {
		// TODO Auto-generated method stub
		
	}



	public int getLogtype() {
		return logtype;
	}



	public void setLogtype(int logtype) {
		this.logtype = logtype;
	}



	public String getSessionId() {
		return sessionId;
	}



	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	
	

	
	
}
