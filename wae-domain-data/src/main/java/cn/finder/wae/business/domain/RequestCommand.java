package cn.finder.wae.business.domain;

import java.io.Serializable;

public class RequestCommand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3327131981716573521L;
	private Long id;
	private String name;
	private String command;
	private String remark;
	
	
	public RequestCommand(){
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
