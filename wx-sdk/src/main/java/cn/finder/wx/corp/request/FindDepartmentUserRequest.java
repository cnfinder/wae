package cn.finder.wx.corp.request;

import cn.finder.wx.corp.response.FindDepartmentUserResponse;
import cn.finder.wx.request.WeixinRequest;

public class FindDepartmentUserRequest extends WeixinRequest<FindDepartmentUserResponse> {

	
	public final static Integer FETCH_CHILD_YES=1;
	public final static Integer FETCH_CHILD_NO=0;
	
	
	public final static Integer STATUS_ALL=0;//0获取全部成员
	public final static Integer STATUS_ATTENTIONED=1;//1获取已关注成员列表
	public final static Integer STATUS_DISABLE=2;//获取禁用成员列表
	public final static Integer STATUS_UNATTENTION=4;//获取未关注成员列表
	
	private Integer department_id;//获取的部门id
	
	private Integer fetch_child=0;//1/0：是否递归获取子部门下面的成员
	
	private Integer status=0;//0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加

	public Integer getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(Integer department_id) {
		this.department_id = department_id;
	}

	public Integer getFetch_child() {
		return fetch_child;
	}

	public void setFetch_child(Integer fetch_child) {
		this.fetch_child = fetch_child;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
