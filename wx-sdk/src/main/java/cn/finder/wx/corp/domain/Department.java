package cn.finder.wx.corp.domain;

public class Department {

	private int id;
	
	/***
	 * 部门名称
	 */
	private String name;
	
	
	private int parentid;
	
	/***
	 * 在父部门中的次序值。order值小的排序靠前。
	 */
	private int order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public Department toTree(){
		throw new RuntimeException("部门树形");
	}
	
	
}
