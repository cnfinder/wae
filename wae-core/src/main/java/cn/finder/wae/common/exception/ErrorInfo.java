package cn.finder.wae.common.exception;

public class ErrorInfo {

	private String errId;
	
	private String errMsg;

	public static String DEFAULT_ERRID="common_errid";
	
	
	public ErrorInfo() {
		super();
	}




	public ErrorInfo(String errId, String errMsg) {
		super();
		this.errId = errId;
		this.errMsg = errMsg;
	}




	public String getErrId() {
		return errId;
	}




	public void setErrId(String errId) {
		this.errId = errId;
	}




	public String getErrMsg() {
		return errMsg;
	}




	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	
	
}
