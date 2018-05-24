package cn.finder.wx.corp.request;

import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.corp.response.MsgSendResponse;

/***
 * 主动发送消息基类
 * @author whl
 *
 */
public abstract class MsgSendBaseRequest extends JsonStringRequest<MsgSendResponse> {

	private String touser;
	
	private String toparty;
	
	private String totag;
	
	
	private String agentid;


	public String getTouser() {
		return touser;
	}


	public void setTouser(String touser) {
		this.touser = touser;
	}


	public String getToparty() {
		return toparty;
	}


	public void setToparty(String toparty) {
		this.toparty = toparty;
	}


	public String getTotag() {
		return totag;
	}


	public void setTotag(String totag) {
		this.totag = totag;
	}


	public String getAgentid() {
		return agentid;
	}


	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	
	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
}
