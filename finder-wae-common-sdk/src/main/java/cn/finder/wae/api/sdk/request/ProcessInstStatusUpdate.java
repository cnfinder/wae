package cn.finder.wae.api.sdk.request;

import cn.finder.httpcommons.request.DefaultRequest;
import cn.finder.httpcommons.response.ApiResponse;

public class ProcessInstStatusUpdate extends DefaultRequest<ApiResponse> {

	
	private static String STATUS_ACTIVE="1";
	private static String STATUS_SUSPEND="0";
	
	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

	
	private String processInstanceId;
	private String status;
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
