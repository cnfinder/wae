package cn.finder.wae.business.module.notification.service.impl;

import java.util.List;

import cn.finder.wae.business.domain.NotificationInfo;
import cn.finder.wae.business.domain.RCInfo;
import cn.finder.wae.business.module.notification.dao.NotificationDao;
import cn.finder.wae.business.module.notification.service.NotificationService;

public class NotificationServiceImpl implements NotificationService{

	private NotificationDao notificationDao;
	
	public void setNotificationDao(NotificationDao notificationDao)
	{
		this.notificationDao = notificationDao;
	}
	
	@Override
	public List<NotificationInfo> queryAllNotificationInfos() {
		// TODO Auto-generated method stub
		return notificationDao.queryAllNotificationInfos();
	}

	@Override
	public List<NotificationInfo> queryNotificationByTargetId(String targetId) {
		// TODO Auto-generated method stub
		return notificationDao.queryNotificationByTargetId(targetId);
	}

	@Override
	public List<NotificationInfo> queryNotificationInfosByUserId(long userId) {
		// TODO Auto-generated method stub
		return notificationDao.queryNotificationInfosByUserId(userId);
	}

	@Override
	public List<NotificationInfo> queryOnlineNotificationInfos(
			List<String> onlineUserNames) {
		// TODO Auto-generated method stub
		return notificationDao.queryOnlineNotificationInfos(onlineUserNames);
	}

	@Override
	public int deleteNotificationById(long id) {
		return notificationDao.deleteNotificationById(id);
	}

	
	public NotificationInfo saveNotificationForSameNT(NotificationInfo notificationInfo) {
		List<NotificationInfo> notifications = notificationDao.queryNotificationByTargetIdAndNiType(notificationInfo.getToUser(),notificationInfo.getNotificationType());
		if(notifications!=null && notifications.size()>0)
		{
			notificationInfo.setId(notifications.get(0).getId());
			//已经存在，那么就更新
			notificationDao.update(notificationInfo);
		}
		else{
			//插入
			notificationInfo=notificationDao.insert(notificationInfo);
		}
		
		return notificationInfo;
	}


	@Override
	public NotificationInfo saveNotification(NotificationInfo notificationInfo) {
		//插入
		notificationInfo=notificationDao.insert(notificationInfo);
		return notificationInfo;
	}

	@Override
	public RCInfo queryRCInfoByTargetCode(String targetCode) {
		// TODO Auto-generated method stub
		return notificationDao.queryRCInfoByTargetCode(targetCode);
	}

	@Override
	public int updateRCInfoAccountByTargetCode(String rcUserId, String rcPwd,
			String targetCode) {
		// TODO Auto-generated method stub
		
		RCInfo rcInfo = notificationDao.queryRCInfoByRCUserId(rcUserId);
		if(rcInfo==null){
			rcInfo= new RCInfo();
			rcInfo.setRcUserId(rcUserId);
			rcInfo.setRcPwd(rcPwd);
			rcInfo.setTargetCode(targetCode);
			return notificationDao.insertRCInfoAccount(rcInfo);
		}else
			return notificationDao.updateRCInfoAccountByTargetCode(rcUserId, rcPwd, targetCode);
	}

	
	
	
}
