package cn.finder.wae.business.domain.scannercode;

public class ScannerCode {

	private int id;
	private String name;
	private String commandName;
	
	private long showtableConfigId;
	
	private String commandMsgTemplateMsgCode;
	
	private String templateMsgCode;
	
	private String field;
	
	private String remark;

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

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public long getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}

	public String getCommandMsgTemplateMsgCode() {
		return commandMsgTemplateMsgCode;
	}

	public void setCommandMsgTemplateMsgCode(String commandMsgTemplateMsgCode) {
		this.commandMsgTemplateMsgCode = commandMsgTemplateMsgCode;
	}

	public String getTemplateMsgCode() {
		return templateMsgCode;
	}

	public void setTemplateMsgCode(String templateMsgCode) {
		this.templateMsgCode = templateMsgCode;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
