package cn.finder.wx.corp.response;

import java.util.List;

import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;
import cn.finder.wx.corp.domain.Agent;
import cn.finder.wx.response.WeixinResponse;

public class FindAgentsResponse extends WeixinResponse{
	private List<Agent> entities;
	@JsonArrayAttribute(name="agentlist")
	@JsonArrayItemAttribute(clazzType=Agent.class)
	public	void setEntities(List<Agent> entities)
	{
		this.entities = entities;
	}
	public List<Agent> getEntities() {
		return entities;
	}
	
}
