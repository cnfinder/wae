package cn.finder.wae.common.mail.template;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.mail.template.parser.TemplateParser;

public class ActivityEmailTemplateParser extends TemplateParser{

	private ActivityEmailPara para;
	
	public ActivityEmailTemplateParser(ActivityEmailPara para)
	{
		this.para = para;
	}
	
	
	
	@Override
	public Map<String, String> findMapPara() {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", para.getUserName());
		map.put("sitename", para.getSiteName());
		map.put("adminemail", para.getAdminEmail());
		map.put("bbname", para.getBbname());
		map.put("time", Common.formatDate(para.getTime()));
		map.put("useracct", para.getUseracct());
		map.put("activityUrl", para.getActivityUrl());
		return map;
	}
	
	
	public static class ActivityEmailPara {

		private String userName;
		
		private String siteName;
		
		private String adminEmail;
		
		private String bbname;
		
		private Date time;
		
		private String activityUrl;
		
		private String useracct;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getSiteName() {
			return siteName;
		}

		public void setSiteName(String siteName) {
			this.siteName = siteName;
		}

		public String getAdminEmail() {
			return adminEmail;
		}

		public void setAdminEmail(String adminEmail) {
			this.adminEmail = adminEmail;
		}

		public String getBbname() {
			return bbname;
		}

		public void setBbname(String bbname) {
			this.bbname = bbname;
		}

		public Date getTime() {
			return time;
		}

		public void setTime(Date time) {
			this.time = time;
		}

		public String getActivityUrl() {
			return activityUrl;
		}

		public void setActivityUrl(String activityUrl) {
			this.activityUrl = activityUrl;
		}

		public String getUseracct() {
			return useracct;
		}

		public void setUseracct(String useracct) {
			this.useracct = useracct;
		}
		
		
		
	}






	
}
