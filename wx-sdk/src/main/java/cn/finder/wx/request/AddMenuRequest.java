package cn.finder.wx.request;

import cn.finder.wx.domain.Body;
import cn.finder.wx.response.AddMenuResponse;

/**
 * 添加菜单请求
 * @author lizhi
 *
 */

@Deprecated
public class AddMenuRequest extends WeixinRequest<AddMenuResponse> {

	private Body body;

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}


	
}
