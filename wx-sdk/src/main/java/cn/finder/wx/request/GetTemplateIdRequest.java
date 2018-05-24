package cn.finder.wx.request;

import cn.finder.wx.response.GetTemplateIdResponse;

/***
 * 获得模板ID
 * @author whl
 *
 */
public class GetTemplateIdRequest extends WeixinRequest<GetTemplateIdResponse>{

	private String template_id_short;

	public String getTemplate_id_short() {
		return template_id_short;
	}

	public void setTemplate_id_short(String template_id_short) {
		this.template_id_short = template_id_short;
	}
	
	
}
