package cn.finder.third.sms.response;

import cn.finder.httpcommons.response.ApiResponse;



public class SendSmsResponse extends ApiResponse{

	
	private String returnstatus;
	
	
	
	private String remainpoint;
	
	private String taskID;
	
	private String successCounts;

	public String getReturnstatus() {
		return returnstatus;
	}

	public void setReturnstatus(String returnstatus) {
		this.returnstatus = returnstatus;
	}


	public String getRemainpoint() {
		return remainpoint;
	}

	public void setRemainpoint(String remainpoint) {
		this.remainpoint = remainpoint;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getSuccessCounts() {
		return successCounts;
	}

	public void setSuccessCounts(String successCounts) {
		this.successCounts = successCounts;
	}
	
	
	
	
}
