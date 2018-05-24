package cn.finder.wae.business.domain;

import java.io.Serializable;

public class PageIndex implements Serializable{

	private int id;
	
	private String name;
	private String pageCode;
	
	private String loginPage;
	
	private String manageIndexPage;
	
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getManageIndexPage() {
		return manageIndexPage;
	}

	public void setManageIndexPage(String manageIndexPage) {
		this.manageIndexPage = manageIndexPage;
	}
	
	
}
