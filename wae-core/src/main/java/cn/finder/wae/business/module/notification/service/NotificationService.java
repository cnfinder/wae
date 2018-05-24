package cn.finder.wae.business.module.notification.service;

import java.util.List;

import cn.finder.wae.business.domain.NotificationInfo;
import cn.finder.wae.business.domain.RCInfo;

public interface NotificationService {
	
	/**
	 * 查找在线的用户
	 * @param onlineUserNames can not be null size >0
	 * @return
	 */
	List<NotificationInfo> queryOnlineNotificationInfos(List<String> onlineUserNames);

	List<NotificationInfo> queryAllNotificationInfos();
	
	List<NotificationInfo> queryNotificationInfosByUserId(long userId);
	
	List<NotificationInfo> queryNotificationByTargetId(String imie);
	
	int deleteNotificationById(long id);
	
	
	
	NotificationInfo saveNotification(NotificationInfo notificationInfo);
	
	
	
	/****
	 * 获取远程控制密码
	 * @param targetCode  目标机器编码，对应  apn-> username
	 * @return
	 */
	RCInfo queryRCInfoByTargetCode(String targetCode);
	
	
	/****
	 * 根据目标编码  修改远控账户
	 * @param rmUserId
	 * @param rcPwd
	 * @param targetCode
	 * @return
	 */
	int updateRCInfoAccountByTargetCode(String rcUserId,String rcPwd,String targetCode);
	
}
