package cn.finder.wx.request;

import java.util.Map;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.SendTempateMsgResponse;

/***
 * 发送模板消息
 * @author whl
 *
 */
public class SendTempateMsgRequest extends JsonStringRequest<SendTempateMsgResponse>{

	private String touser;
	
	private String template_id;
	
	private String url;
	
	private String topcolor;
	
	
	private Map<String,TemplateData> data;
	/***
	 * 模板数据
	 * @author whl
	 *
	 */
	public static class TemplateData extends ApiObject{
		
		private String value;
		private String color;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		
		
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTopcolor() {
		return topcolor;
	}
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	public Map<String, TemplateData> getData() {
		return data;
	}
	public void setData(Map<String, TemplateData> data) {
		this.data = data;
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
