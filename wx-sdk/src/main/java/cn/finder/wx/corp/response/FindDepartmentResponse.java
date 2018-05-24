package cn.finder.wx.corp.response;

import java.util.List;

import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;
import cn.finder.wx.corp.domain.Department;
import cn.finder.wx.response.WeixinResponse;

public class FindDepartmentResponse extends WeixinResponse {
	
	private List<Department> department;
	@JsonArrayAttribute(name="department")
	@JsonArrayItemAttribute(clazzType=Department.class)
	public	void setDepartment(List<Department> department)
	{
		this.department = department;
	}
	public List<Department> getDepartment() {
		return department;
	}
	
	
	public Department getDepartmentTree(){
		return null;
	}
	
	
	
}
