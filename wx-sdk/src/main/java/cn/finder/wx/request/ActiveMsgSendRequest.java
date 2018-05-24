package cn.finder.wx.request;

import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.ActiveMsgSendResponse;

/***
 * 主动消息发送请求基类
 * @author whl
 *
 */
public class ActiveMsgSendRequest extends JsonStringRequest<ActiveMsgSendResponse>{

	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

	

	 /// <summary>
    /// UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
    /// </summary>
    public String touser;

    /// <summary>
    /// PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
    ///可以是 部门ID
    /// </summary>
    public String toparty;

    /// <summary>
    /// TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
    /// </summary>
    public String totag;

    /// <summary>
    /// 消息类型
    /// </summary>
    public String msgtype;

    /// <summary>
    /// 企业应用的id，整型。可在应用的设置页面查看
    /// </summary>
    public String agentid;

    /// <summary>
    /// 表示是否是保密消息，0表示否，1表示是，默认0
    /// </summary>
    public String safe="0";

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

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

	
	
	
}
