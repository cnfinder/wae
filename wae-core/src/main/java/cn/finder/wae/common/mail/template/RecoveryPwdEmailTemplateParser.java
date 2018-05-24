package cn.finder.wae.common.mail.template;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.mail.template.parser.TemplateParser;

public class RecoveryPwdEmailTemplateParser extends TemplateParser{

	private RecoveryPwdEmailPara para;
	
	public RecoveryPwdEmailTemplateParser(RecoveryPwdEmailPara para)
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
		map.put("userpwd", para.getUserPwd());
		return map;
	}
	
	
	public static class RecoveryPwdEmailPara {

		private String userName;
		
		private String siteName;
		
		private String adminEmail;
		
		private String bbname;
		
		private Date time;
		
		private String userPwd;
		
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

	

		public String getUserPwd() {
			return userPwd;
		}

		public void setUserPwd(String userPwd) {
			this.userPwd = userPwd;
		}

		public String getUseracct() {
			return useracct;
		}

		public void setUseracct(String useracct) {
			this.useracct = useracct;
		}
		
		
		
	}






	
}
