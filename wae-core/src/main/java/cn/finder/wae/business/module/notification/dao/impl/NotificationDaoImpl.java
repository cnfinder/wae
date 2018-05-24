package cn.finder.wae.business.module.notification.dao.impl;


import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import cn.finder.wae.business.domain.NotificationInfo;
import cn.finder.wae.business.domain.RCInfo;
import cn.finder.wae.business.module.notification.dao.NotificationDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class NotificationDaoImpl extends BaseJdbcDaoSupport implements NotificationDao{

	@Override
	public List<NotificationInfo> queryAllNotificationInfos() {
		// TODO Auto-generated method stub
		String sql = "select * from v_notification where isread =0";
		return getJdbcTemplate().query(sql, new RowMapperFactory.NotificationInfoRowMapper());
	}

	@Override
	public List<NotificationInfo> queryNotificationByTargetId(String targetId) {
		String sql = "select * from v_notification where  isread =0 and  to_user=?";
		return getJdbcTemplate().query(sql,new Object[]{targetId}, new RowMapperFactory.NotificationInfoRowMapper());
	}

	@Override
	public List<NotificationInfo> queryNotificationInfosByUserId(long userId) {
		String sql = "select * from v_notification where isread =0 and to_user=?";
		return getJdbcTemplate().query(sql,new Object[]{userId}, new RowMapperFactory.NotificationInfoRowMapper());
	}

	@Override
	public List<NotificationInfo> queryOnlineNotificationInfos(
			List<String> onlineUserNames) {
		if(onlineUserNames ==null || onlineUserNames.isEmpty())
		{
			return null;
		}
		String sql = "select * from v_notification where isread=0 ";
		
		String users = StringUtils.join(onlineUserNames, ",");
		sql +=" and to_user in (?)";
		
		return getJdbcTemplate().query(sql,new Object[]{users}, new RowMapperFactory.NotificationInfoRowMapper());
	}

	@Override
	public int deleteNotificationById(long id) {
		String sql ="delete from t_notification where id = ?";
		return getJdbcTemplate().update(sql, new Object[]{id});
	}

	@Override
	public NotificationInfo insert(final NotificationInfo info) {
		String sql = "{call proc_save_notification(?,?,?,?,?,?,?,?)}";
		  long ni_id = getJdbcTemplate().execute(sql,new CallableStatementCallback<Long>(){  
		         public Long doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {  
		        	 cs.setString(1, info.getFromUser());
						cs.setString(2, info.getToUser());
						if(info.getUpdateTime()==null)
							info.setUpdateTime(new Date());
						cs.setTimestamp(3, new java.sql.Timestamp(info.getUpdateTime().getTime()));
						cs.setString(4, info.getTitle());
						cs.setLong(5, info.getNotificationType());
						cs.setString(6, info.getNotificationMsg());
						cs.setString(7, info.getUrl());
						cs.registerOutParameter(8, Types.BIGINT);
						
		             try {
						cs.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}  
		             long ni_id = cs.getLong(10);  
		             
		             return ni_id;
		         }     
		     });  
		  
		    info.setId(ni_id);
		    return info;
	}

	@Override
	public int update(final NotificationInfo info) {
		
		String sql = "{call proc_update_notification(?,?,?,?,?,?,?,?,?)}";
		  int cnt = getJdbcTemplate().execute(sql,new CallableStatementCallback<Integer>(){  
		         public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {  
		        	 cs.setString(1, info.getFromUser());
						cs.setString(2, info.getToUser());
						cs.setLong(3, info.getUserId());
						cs.setInt(4,info.getIsRead());
						
						cs.setTimestamp(5, new  java.sql.Timestamp(new Date().getTime()));
						cs.setString(6, info.getNotificationMsg());
						cs.setString(7, info.getTitle());
						cs.setString(8, info.getUrl());
//						cs.setLong(9, info.getNotificationType());
						cs.setLong(9, info.getId());
						
		              
		             
		             return cs.executeUpdate();
		         }     
		     });  
		  
		    return cnt;
	}

	@Override
	public List<NotificationInfo> queryNotificationByTargetIdAndNiType(
			String imie, long notificationType) {
		String sql = "select * from v_notification where  isread =0 and  to_user=? and notification_type_id=?";
		return getJdbcTemplate().query(sql,new Object[]{imie,notificationType}, new RowMapperFactory.NotificationInfoRowMapper());
	}

	@Override
	public RCInfo queryRCInfoByTargetCode(String targetCode) {
		String sql ="select * from  v_rcinfo where target_code = ?";
		
		return queryForSingle(sql, new Object[]{targetCode}, new RowMapperFactory.RCInfoRowMapper());
	}

	@Override
	public int updateRCInfoAccountByTargetCode(String rcUserId, String rcPwd,
			String targetCode) {
		String sql ="update t_rc_info set rc_userid =?,rc_pwd=?,update_date=? where target_code=?";
		return getJdbcTemplate().update(sql, new Object[]{rcUserId,rcPwd,new Date(),targetCode});
	}


	
	public int insertRCInfoAccount(RCInfo rcInfo){
		String sql ="insert into t_rc_info(rc_userid,rc_pwd,target_code,create_date,update_date) values(?,?,?,?,?)";
		
		return getJdbcTemplate().update(sql, new Object[]{rcInfo.getRcUserId(),rcInfo.getRcPwd(),rcInfo.getTargetCode(),new Date(),new Date()});
	}

	@Override
	public RCInfo queryRCInfoByRCUserId(String rcUserId) {
		String sql ="select * from v_rcinfo where rc_userid=?";
		return queryForSingle(sql, new Object[]{rcUserId}, new RowMapperFactory.RCInfoRowMapper());
	}
	
}
