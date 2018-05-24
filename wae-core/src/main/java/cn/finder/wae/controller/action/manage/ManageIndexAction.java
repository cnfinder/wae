package cn.finder.wae.controller.action.manage;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.common.constant.Constant;

@SuppressWarnings("all")
public class ManageIndexAction  extends BaseActionSupport{
	private static final long serialVersionUID = 1L;
	
	private static Logger logger=Logger.getLogger(ManageIndexAction.class);
	
	private User user;
	
	private String goPage;
	
	private String pcode="101";//平台页面编码
	
	public String manageIndex()
	{
		user = (User)session.getAttribute(Constant.KEY_SESSION_USER);
		//managePage=ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_CONFIG_MANAGE_MAIN_PAGE).getValue();
		if(StringUtils.isEmpty(pcode)){
			pcode="101";
		}
		try{
			goPage=ArchCache.getInstance().getPageIndexCache().get(pcode).getManageIndexPage();
		}
		catch(Exception e){
			pcode="101";
		}
		return SUCCESS;
	}
	
	
	public String adminTop()
	{
		user = (User)session.getAttribute(Constant.KEY_SESSION_USER);
		return SUCCESS;
	}
	public String adminSplit()
	{
		return SUCCESS;
	}

	public String adminLeft()
	{
		user = (User)session.getAttribute(Constant.KEY_SESSION_USER);
		return SUCCESS;
	}
	public String adminRight()
	{
		return SUCCESS;
	}

	
	
	public String profile(){
		return SUCCESS;
	}
	

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	


	public String getGoPage() {
		return goPage;
	}


	public void setGoPage(String goPage) {
		this.goPage = goPage;
	}


	public void setPcode(String pcode) {
		this.pcode = pcode;
	}


	public String getPcode() {
		return pcode;
	}


	
}
