package cn.finder.wae.business.domain;

import java.util.Date;

public class FileMsg {
	private Long id;
	private String name;
	private String filePath;
	private String fileType;
	private Long fileSize;
	private Long uploader;
	private Long parentId;
	private Long downloadTimes;
	private Date createDate;
	private Date updateDate;
	
	public FileMsg(){
		
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Long getUploader() {
		return uploader;
	}
	public void setUploader(Long uploader) {
		this.uploader = uploader;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getDownloadTimes() {
		return downloadTimes;
	}
	public void setDownloadTimes(Long downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
}
