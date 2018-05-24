package cn.finder.wae.business.module.notification.dao;

import java.util.List;

import cn.finder.wae.business.domain.NotificationInfo;
import cn.finder.wae.business.domain.RCInfo;

public interface NotificationDao {
	/****
	 * 查询在线的用户的消息
	 * @param onlineUserNames
	 * @return
	 */
	List<NotificationInfo> queryOnlineNotificationInfos(List<String> onlineUserNames);
	
	
	/****
	 * 查询所有的消息
	 * @return
	 */
	List<NotificationInfo> queryAllNotificationInfos();
	
	
	/***
	 * 获取指定用户消息
	 * @param userId
	 * @return
	 */
	@Deprecated
	List<NotificationInfo> queryNotificationInfosByUserId(long userId);
	
	
	/****
	 * 查询发给目标设备或用户消息
	 * @param imie
	 * @return
	 */
	List<NotificationInfo> queryNotificationByTargetId(String targetId);
	
	
	
	@Deprecated
	List<NotificationInfo> queryNotificationByTargetIdAndNiType(String imie,long notificationType);
	
	
	/***
	 * 删除消息
	 * @param id
	 * @return
	 */
	int deleteNotificationById(long id);
	
	
	/***
	 * 添加消息到消息区
	 * @param info
	 * @return
	 */
	NotificationInfo insert(NotificationInfo info);
	
	
	
	/****
	 * 更新消息
	 * @param info
	 * @return
	 */
	int update(NotificationInfo info);
	//NotificationInfo saveNotification(NotificationInfo notificationInfo);
	
	
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
	
	
	/***
	 * 根据userid 获取
	 * @param rcUserId
	 * @return
	 */
	RCInfo queryRCInfoByRCUserId(String rcUserId);
	
	
	/****
	 * 保存获取远控账户
	 * @param rcInfo
	 * @return
	 */
	int insertRCInfoAccount(RCInfo rcInfo);
}
