package cn.finder.wae.api.sdk.response;

import java.util.List;

import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;
import cn.finder.httpcommons.response.ApiResponse;
import cn.finder.wae.api.sdk.domain.User;

public class UserSearchResponse extends ApiResponse {

	private List<User> entities;

	public List<User> getEntities() {
		return entities;
	}

	
	@JsonArrayAttribute(name="entities")
	@JsonArrayItemAttribute(name="",clazzType=User.class)
	public void setEntities(List<User> entities) {
		this.entities = entities;
	}
	
	
}
