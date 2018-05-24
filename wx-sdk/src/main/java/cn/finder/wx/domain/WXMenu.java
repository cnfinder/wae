package cn.finder.wx.domain;

import java.util.ArrayList;
import java.util.List;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.attri.JsonProperty;

public class WXMenu  extends ApiObject{

	private int id;
	
	private String name;
	
	private String type;
	
	private String key;
	
	private String url;
	
	@org.codehaus.jackson.annotate.JsonProperty("sub_button")
	private List<WXMenu> subButton=new ArrayList<WXMenu>();
	
	

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

	//@JsonProperty(name="sub_button")
	public List<WXMenu> getSubButton() {
		return subButton;
	}

	public void setSubButton(List<WXMenu> subButton) {
		this.subButton = subButton;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
