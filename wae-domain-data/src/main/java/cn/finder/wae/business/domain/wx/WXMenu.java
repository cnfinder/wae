package cn.finder.wae.business.domain.wx;

import java.util.ArrayList;
import java.util.List;

import cn.finder.httpcommons.attri.JsonProperty;

/***
 * 菜单
 * @author whl
 *
 */
public class WXMenu {

	
	public static String MENUTYPE_CLICK="click";
	public static String MENUTYPE_VIEW="view";
	
	
	
	public static String KEY_KEY1="key_001";
	
	private int id;
	
	private String name;
	
	private String type;
	
	private String key;
	
	private String url;
	
	private String mediaId;
	
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

	@JsonProperty(name = "sub_button")
	public List<WXMenu> getSubButton() {
		return subButton;
	}

	public void setSubButton(List<WXMenu> subButton) {
		this.subButton = subButton;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	
	
}
