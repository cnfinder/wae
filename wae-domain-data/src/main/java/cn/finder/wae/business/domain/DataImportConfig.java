package cn.finder.wae.business.domain;

import java.sql.Timestamp;

public class DataImportConfig {
	public Long id;
	private String name;
	private Long processShowTableConfigId;
	private Long sourceShowTableConfigId;
	private Long destShowTableConfigId;
	private Long frequency;
	private String remark;
	private int isRunning;
	private int startWithServer;
	private int dynamic;
	private int isBackUp;
	private int startTime;
	private int isImport;
	private int isDelete;
	private int reportId;
	
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
	public Long getProcessShowTableConfigId() {
		return processShowTableConfigId;
	}
	public void setProcessShowTableConfigId(Long processShowTableConfigId) {
		this.processShowTableConfigId = processShowTableConfigId;
	}
	public Long getSourceShowTableConfigId() {
		return sourceShowTableConfigId;
	}
	public void setSourceShowTableConfigId(Long sourceShowTableConfigId) {
		this.sourceShowTableConfigId = sourceShowTableConfigId;
	}
	public Long getDestShowTableConfigId() {
		return destShowTableConfigId;
	}
	public void setDestShowTableConfigId(Long destShowTableConfigId) {
		this.destShowTableConfigId = destShowTableConfigId;
	}
	public Long getFrequency() {
		return frequency;
	}
	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsRunning() {
		return isRunning;
	}
	public void setIsRunning(int isRunning) {
		this.isRunning = isRunning;
	}
	public int getDynamic() {
		return dynamic;
	}
	public void setDynamic(int dynamic) {
		this.dynamic = dynamic;
	}
	public int getStartWithServer() {
		return startWithServer;
	}
	public void setStartWithServer(int startWithServer) {
		this.startWithServer = startWithServer;
	}
	public int getIsBackUp() {
		return isBackUp;
	}
	public void setIsBackUp(int isBackUp) {
		this.isBackUp = isBackUp;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getIsImport() {
		return isImport;
	}
	public void setIsImport(int isImport) {
		this.isImport = isImport;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	
	
}
