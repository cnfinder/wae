package cn.finder.wae.business.domain.wx.corp;

/***
 * 企业号管理组
 * @author whl
 *
 */
public class CorpAdminGroup {

	private int corpinfo_id;
	
	private String corp_id;
	
	private String secret;
	
	private int adminGroupTypeId;
	
	private String adminGroupTypeName;
	
	private String adminGroupTypeCode;
	
	private String corpinfo_name;

	

	public int getCorpinfo_id() {
		return corpinfo_id;
	}

	public void setCorpinfo_id(int corpinfo_id) {
		this.corpinfo_id = corpinfo_id;
	}

	public String getCorp_id() {
		return corp_id;
	}

	public void setCorp_id(String corp_id) {
		this.corp_id = corp_id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getAdminGroupTypeId() {
		return adminGroupTypeId;
	}

	public void setAdminGroupTypeId(int adminGroupTypeId) {
		this.adminGroupTypeId = adminGroupTypeId;
	}

	public String getAdminGroupTypeName() {
		return adminGroupTypeName;
	}

	public void setAdminGroupTypeName(String adminGroupTypeName) {
		this.adminGroupTypeName = adminGroupTypeName;
	}

	public String getAdminGroupTypeCode() {
		return adminGroupTypeCode;
	}

	public void setAdminGroupTypeCode(String adminGroupTypeCode) {
		this.adminGroupTypeCode = adminGroupTypeCode;
	}

	public String getCorpinfo_name() {
		return corpinfo_name;
	}

	public void setCorpinfo_name(String corpinfo_name) {
		this.corpinfo_name = corpinfo_name;
	}
	
	
	
}
