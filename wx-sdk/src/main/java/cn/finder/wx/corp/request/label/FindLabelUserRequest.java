package cn.finder.wx.corp.request.label;

import cn.finder.wx.corp.response.label.FindLabelUserResponse;
import cn.finder.wx.request.WeixinRequest;

public class FindLabelUserRequest extends WeixinRequest<FindLabelUserResponse> {

	
	private String tagid;

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	
	
	
	
}
