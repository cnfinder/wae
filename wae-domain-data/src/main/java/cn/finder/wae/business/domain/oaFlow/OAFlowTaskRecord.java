package cn.finder.wae.business.domain.oaFlow;

public class OAFlowTaskRecord {

	private String business_key; // 业务主键
	private String task_id; // 业务主键
	private String content;
	private String user_id; // OA发起人
	private String task_result;

	public String getTask_result() {
		return task_result;
	}

	public void setTask_result(String task_result) {
		this.task_result = task_result;
	}

	public String getBusiness_key() {
		return business_key;
	}

	public void setBusiness_key(String business_key) {
		this.business_key = business_key;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
