package cn.finder.wae.common.exception;

import java.util.ArrayList;
import java.util.List;

public class Fault {

	private List<ErrorInfo> errs;

	public Fault() {
		super();
		this.errs = new ArrayList<ErrorInfo>();
	}
	
	
	public void addErrorInfo(String errId,String errMsg)
	{
		errs.add(new ErrorInfo(errId, errMsg));
	}


	public List<ErrorInfo> getErrs() {
		return errs;
	}
	
	
}
