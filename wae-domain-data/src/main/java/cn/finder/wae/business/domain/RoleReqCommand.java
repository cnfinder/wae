package cn.finder.wae.business.domain;

import java.sql.Timestamp;

public class RoleReqCommand {
	private Long id;
	private Long role_id;
	private Long request_command_id;
	private Timestamp createDate;
	private Timestamp updateDate;
	
	
	private String requestCommandName;
	
	private String command;
	
	
	public RoleReqCommand(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public Long getRequest_command_id() {
		return request_command_id;
	}

	public void setRequest_command_id(Long request_command_id) {
		this.request_command_id = request_command_id;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getRequestCommandName() {
		return requestCommandName;
	}

	public void setRequestCommandName(String requestCommandName) {
		this.requestCommandName = requestCommandName;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
