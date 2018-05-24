package cn.finder.wx.domain;

import java.util.ArrayList;
import java.util.List;

import cn.finder.httpcommons.ApiObject;

public class Body extends ApiObject{

	private List<WXMenu> button=new ArrayList<WXMenu>();

	public List<WXMenu> getButton() {
		return button;
	}

	public void setButton(List<WXMenu> button) {
		this.button = button;
	}

	
	
	
	
}
