package cn.finder.wae.controller.action.common;

import cn.finder.wae.common.base.BaseActionSupport;

public class MsgAction extends BaseActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1241160310380961663L;

	
	private String msg;
	
	public String noAuth(){
		return SUCCESS;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
