package cn.finder.wae.common.exception;

import java.io.Serializable;

public class MsgInfo implements Serializable {

	private static final long serialVersionUID = -6176601676263544194L;
	private String errId;
	private String errDesc;

	public MsgInfo() {
		super();
	}

	public MsgInfo(String errId) {
		this.errId = errId;
	}

	public MsgInfo(String errId, String errDesc) {
		this.errId = errId;
		this.errDesc = errDesc;
	}

	// ------ getter and setters --------

	public String getErrId() {
		return errId;
	}

	public void setErrId(String errId) {
		this.errId = errId;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}
	
	@Override
	public String toString(){
		return  String.format("errId:%s,errDesc:%s", errId,errDesc);
	}

}
