package cn.finder.wae.business.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class UserSetting {

	private int id;
	
	private int isLeftCollapse=0;
	
	private String manageIndexPage="";
	
	private String userName;
	private int browserMax=0;
	
	private String firstMenuPage="";
	
	private Menu firstMenu;
	
	private String extSetting;
	
	private Map<String,Object> extSettingMap = new HashMap<String, Object>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsLeftCollapse() {
		return isLeftCollapse;
	}

	public void setIsLeftCollapse(int isLeftCollapse) {
		this.isLeftCollapse = isLeftCollapse;
	}

	public String getManageIndexPage() {
		return manageIndexPage;
	}

	public void setManageIndexPage(String manageIndexPage) {
		this.manageIndexPage = manageIndexPage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	public int getBrowserMax() {
		return browserMax;
	}

	public void setBrowserMax(int browserMax) {
		this.browserMax = browserMax;
	}

	public String getFirstMenuPage() {
		return firstMenuPage;
	}

	public void setFirstMenuPage(String firstMenuPage) {
		this.firstMenuPage = firstMenuPage;
	}

	public String getExtSetting() {
		return extSetting;
	}

	public void setExtSetting(String extSetting) {
		this.extSetting = extSetting;
		if(!StringUtils.isEmpty(extSetting)){
			String[] exts = extSetting.split(";");
			if(exts!=null && exts.length>0){
				for(int i=0;i<exts.length;i++){
					String ext = exts[i];
					String[] aa = ext.split("=");
					extSettingMap.put(aa[0], aa[1]);
				}
					
			}
		}
		
	}

	public Map<String, Object> getExtSettingMap() {
		return extSettingMap;
	}

	public Menu getFirstMenu() {
		return firstMenu;
	}

	public void setFirstMenu(Menu firstMenu) {
		this.firstMenu = firstMenu;
	}
	
	
	
	
	
	
}
