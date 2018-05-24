package cn.finder.wx.corp.response;

import java.util.List;

import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;
import cn.finder.wx.response.WeixinResponse;

public class FindAgentInfoResponse extends WeixinResponse {

	
	private String agentid;
	private String name;
	private String square_logo_url;
	private String round_logo_url;
	
	private Allow_userinfos allow_userinfos;
	
	
	public String getAgentid() {
		return agentid;
	}



	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}


	public String getName() {
		return name;
	}







	public void setName(String name) {
		this.name = name;
	}







	public String getSquare_logo_url() {
		return square_logo_url;
	}

	public void setSquare_logo_url(String square_logo_url) {
		this.square_logo_url = square_logo_url;
	}

	public String getRound_logo_url() {
		return round_logo_url;
	}

	public void setRound_logo_url(String round_logo_url) {
		this.round_logo_url = round_logo_url;
	}


	public Allow_userinfos getAllow_userinfos() {
		return allow_userinfos;
	}


	public void setAllow_userinfos(Allow_userinfos allow_userinfos) {
		this.allow_userinfos = allow_userinfos;
	}



	public static class Allow_userinfos{
		
		private List<User> user;

		
		public List<User> getUser() {
			return user;
		}
		@JsonArrayAttribute(name="user")
		@JsonArrayItemAttribute(clazzType=User.class)
		public void setUser(List<User> user) {
			this.user = user;
		}
		
		
		public static class User{
			private String userid;
			private String status;
			public String getUserid() {
				return userid;
			}
			public void setUserid(String userid) {
				this.userid = userid;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			
		}
		
		
	}
}
