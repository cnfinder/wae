package cn.finder.wx.corp.response;

import java.util.List;

import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;
import cn.finder.wx.corp.domain.User;
import cn.finder.wx.response.WeixinResponse;

public class FindDepartmentUserResponse extends WeixinResponse {
	
	private List<User> entities;
	@JsonArrayAttribute(name="userlist")
	@JsonArrayItemAttribute(clazzType=User.class)
	public	void setEntities(List<User> entities)
	{
		this.entities = entities;
	}
	public List<User> getEntities() {
		return entities;
	}
	
	
	
}
