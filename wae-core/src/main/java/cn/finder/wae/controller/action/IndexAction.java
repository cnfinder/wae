package cn.finder.wae.controller.action;

import org.apache.log4j.Logger;

import cn.finder.wae.common.base.BaseActionSupport;

public class IndexAction  extends BaseActionSupport{
	private static final long serialVersionUID = 1L;

	private static Logger logger=Logger.getLogger(IndexAction.class);
	
	public String index()
	{
		return SUCCESS;
	}


}
