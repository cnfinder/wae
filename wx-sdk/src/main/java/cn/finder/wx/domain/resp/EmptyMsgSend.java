package cn.finder.wx.domain.resp;



/***
 * 封装文字类的返回消息
 * @author whl
 *
 */
public class EmptyMsgSend implements MsgSendInterface{

	
	public String toXml() {
		return "success";
	}
	
	
	
}
