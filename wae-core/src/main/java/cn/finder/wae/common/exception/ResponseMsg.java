package cn.finder.wae.common.exception;

public class ResponseMsg {
private boolean ok=false;
	
	private String msg;

	
	public ResponseMsg() {
		super();
	}

	public ResponseMsg(boolean ok,String msg)
	{
		this.ok = ok;
		this.msg = msg;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
