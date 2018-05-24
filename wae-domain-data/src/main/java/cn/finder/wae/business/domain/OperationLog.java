package cn.finder.wae.business.domain;

import java.util.Date;

public class OperationLog {

	 private long id;
	 private String userId;
	 private String userName;//可能为空,后台操作DAO的时候，默认用户名为: system_thread
	 private int logLevel;// 日志级别， 操作日志为 1， 警告日志为2， 错误日志为3
	 private String processClass;  //处理的类完全名称
	 private String methodSignature;  //方法签名(包括参数以及返回值)
	 private String argumentsValue;// JSON或者XML序列化格式,字段名称可以从 showdataconfig中获取（用于ArchType）
	 private String refId; //业务ID ，这里使用 showtableConfigId  以便获取操作名称,一般为第一个参数
	 private String operationName; // 操作名称， 用户定义或者 showtableConfig中获取
	 private Date operationDate;// 操作时间
	 private Date createDate;// 创建日期	
	 private Date updateDate;// 更新日期
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public int getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}
	public String getProcessClass() {
		return processClass;
	}
	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}
	public String getMethodSignature() {
		return methodSignature;
	}
	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	public String getArgumentsValue() {
		return argumentsValue;
	}
	public void setArgumentsValue(String argumentsValue) {
		this.argumentsValue = argumentsValue;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public Date getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	 
	 
	 
}
