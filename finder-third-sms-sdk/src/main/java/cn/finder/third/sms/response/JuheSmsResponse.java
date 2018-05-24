package cn.finder.third.sms.response;

import cn.finder.httpcommons.response.ApiResponse;



public class JuheSmsResponse extends ApiResponse{

	
	private String error_code;
	private String reason;
	
	private Result result;
	
	
	
	public String getError_code() {
		return error_code;
	}



	public void setError_code(String error_code) {
		this.error_code = error_code;
	}



	public String getReason() {
		return reason;
	}



	public void setReason(String reason) {
		this.reason = reason;
	}



	public Result getResult() {
		return result;
	}



	public void setResult(Result result) {
		this.result = result;
	}



	public static class Result{
		private long count;
		
		private long fee;
		
		private String sid;

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		public long getFee() {
			return fee;
		}

		public void setFee(long fee) {
			this.fee = fee;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}
		
		
	}
	
	
}
