package cn.finder.wx.corp.response.label;

import java.util.List;

import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;
import cn.finder.wx.corp.domain.Tag;
import cn.finder.wx.response.WeixinResponse;

public class FindLabelsResponse extends WeixinResponse {
	
	private List<Tag> entities;
	@JsonArrayAttribute(name="taglist")
	@JsonArrayItemAttribute(clazzType=Tag.class)
	public	void setEntities(List<Tag> entities)
	{
		this.entities = entities;
	}
	public List<Tag> getEntities() {
		return entities;
	}
	
	
	
}
