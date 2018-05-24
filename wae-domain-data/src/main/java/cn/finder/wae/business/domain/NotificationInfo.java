package cn.finder.wae.business.domain;

import java.util.Date;

public class NotificationInfo {

	private long id;
	
	private String fromUser;
	
	private String toUser;
	private long userId;
	
	private int isRead;
	
	
	private Date updateTime;
	
	private Date readDate;
	
	private String notificationMsg;
	
	private String title;
	
	private String url;
	
	private long notificationType;

	
	public static final int NOTIFICATIONTYPE_FIND_TEAMVIEWER_USERPASS=1;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getNotificationMsg() {
		return notificationMsg;
	}

	public void setNotificationMsg(String notificationMsg) {
		this.notificationMsg = notificationMsg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(long notificationType) {
		this.notificationType = notificationType;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	
	
	
}
