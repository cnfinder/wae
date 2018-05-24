package cn.finder.wae.business.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.restlet.data.Status;

/**
 * 
 * @author 吴华龙
 *
 */
@XmlRootElement(name="message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message { 

	public static int StatusCode_OK=200; //请求成功
    public static int StatusCode_ClientError = 403;//客户端错误 
    public static int StatusCode_ServerError = 500;//服务器端错误
    public static int StatusCode_ApiNameError = 501;//接口名称不存在或参数错误
    public static int StatusCode_UnAuth = 401;//授权失败
    public static int StatusCode_Disabled = 800;//接口被禁用
    public static int StatusCode_PARAMS_ERROR = 802;//参数错误
    public static int StatusCode_InfoExp = 801;//处理不一定成功，返回提示客户端信息
    
	@XmlElement(name="statusCode")
	private int statusCode=StatusCode_OK;  // 异常方面的状态判断
	
	private int resultCode=StatusCode_OK; // 业务 方面的 状态判断,执行失败的地方重新设置 非 200的值
	
	private String resultMsg="";
	
	@XmlElement(name="msg")
	private String msg;
	
	@XmlElement(name="detail")
	private String detail;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	
	
	
	
	
}
