package cn.finder.wae.business.module.common.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.finder.wae.business.domain.ServiceInterfaceLog;
import cn.finder.wae.business.domain.ShowtableConfigProcess;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.domain.UserSetting;
import cn.finder.wae.business.domain.ble.BleEquipment;
import cn.finder.wae.business.domain.chart.Chart;
import cn.finder.wae.business.domain.oaFlow.OAFlowBase;
import cn.finder.wae.business.domain.oaFlow.OAFlowItem;
import cn.finder.wae.business.domain.oaFlow.OAFlowTaskRecord;
import cn.finder.wae.business.domain.properties.PropertiesTemplateItem;
import cn.finder.wae.business.domain.scannercode.ScannerCode;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.domain.wx.WXAppInfoManager;
import cn.finder.wae.business.domain.wx.WXCommand;
import cn.finder.wae.business.domain.wx.corp.CorpAdminGroup;
import cn.finder.wae.business.domain.wx.corp.CorpInfo;
import cn.finder.wae.business.module.common.dao.CommonDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class CommonDaoImpl extends BaseJdbcDaoSupport implements CommonDao {

	@Override
	public List<SysConfig> loadCache() {

		List<SysConfig> list = getJdbcTemplate().query("select * from v_sys_config", new RowMapperFactory.SysConfigRowMapper());

		return list;
	}

	@Override
	public UserSetting findUserSettingByUserName(String userName) {
		String sql = "select * from v_user_setting where user_name=?";
		return queryForSingle(sql, new Object[] { userName }, new RowMapperFactory.UserSettingRowMapper());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cn.finder.wae.business.module.common.dao.CommonDao#findShowtableConfigProcess
	 * (long)
	 */
	@Override
	public ShowtableConfigProcess findShowtableConfigProcess(long showtableConfigId) {
		String sql = "select * from t_showtable_config_processkey where showtable_config_id=?";
		return queryForSingle(sql, new Object[] { showtableConfigId }, new RowMapperFactory.ShowtableConfigProcessRowMapper());
	}

	@Override
	public int updateUserConfig(String key, String value) {
		String sql = "update t_user_config set value=? where name=?";

		return getJdbcTemplate().update(sql, new Object[] { value, key });
	}

	@Override
	public String findUserConfig(String key) {

		String sql = "select value from t_user_config where name = ?";

		return queryForObject(sql, new Object[] { key }, String.class);
	}

	@Override
	public int addServiceInterfaceLog(ServiceInterfaceLog serviceInterfaceLog) {
		final String sql = "{call p_service_interface_log_add(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		final ServiceInterfaceLog log = serviceInterfaceLog;
		/*
		 * int keyId = getJdbcTemplate().update(sql, new
		 * Object[]{serviceInterfaceLog.getInterfaceName(),
		 * serviceInterfaceLog.getShowtableConfigId(),
		 * serviceInterfaceLog.getInterfaceNameCn(),
		 * serviceInterfaceLog.getIsNeedAuth(),
		 * serviceInterfaceLog.getVersion(), serviceInterfaceLog.getGroupName(),
		 * serviceInterfaceLog.getRemark(), serviceInterfaceLog.getEnabled(),
		 * new Timestamp(serviceInterfaceLog.getInvokeTime().getTime()),
		 * serviceInterfaceLog.getInputContent() });
		 * serviceInterfaceLog.setId(keyId); return 1;
		 */

		long keyId = getJdbcTemplate().execute(sql, new CallableStatementCallback<Long>() {
			public Long doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.setObject(1, log.getInterfaceName());
				cs.setObject(2, log.getShowtableConfigId());
				cs.setObject(3, log.getInterfaceNameCn());
				cs.setObject(4, log.getIsNeedAuth());
				cs.setObject(5, log.getVersion());
				cs.setObject(6, log.getGroupName());
				cs.setObject(7, log.getRemark());
				cs.setObject(8, log.getEnabled());
				cs.setObject(9, new Timestamp(log.getInvokeTime().getTime()));
				cs.setObject(10, log.getInputContent());
				cs.setObject(11, log.getStatusCode());
				cs.setObject(12, log.getOutMsg());
				cs.setObject(13, new Timestamp(log.getCompleteTime().getTime()));
				cs.registerOutParameter(14, Types.BIGINT);

				try {
					cs.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				long ni_id = cs.getLong(14);

				return ni_id;
			}
		});
		log.setId(keyId);

		return 1;
		/*
		 * final ServiceInterfaceLog log = serviceInterfaceLog;
		 * KeyHolder keyHolder = new GeneratedKeyHolder();
		 * int effect = getJdbcTemplate().update(new PreparedStatementCreator()
		 * { public PreparedStatement createPreparedStatement(Connection con)
		 * throws SQLException { PreparedStatement ps =
		 * con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		 * ps.setObject(1,log.getInterfaceName() ); ps.setObject(2,
		 * log.getShowtableConfigId()); ps.setObject(3,
		 * log.getInterfaceNameCn()); ps.setObject(4, log.getIsNeedAuth());
		 * ps.setObject(5, log.getVersion()); ps.setObject(6,log.getGroupName()
		 * ); ps.setObject(7, log.getRemark()); ps.setObject(8,
		 * log.getEnabled()); ps.setObject(9,new
		 * Timestamp(log.getInvokeTime().getTime())); ps.setObject(10,
		 * log.getInputContent());
		 * return ps; } }, keyHolder);
		 * int v_key = keyHolder.getKey().intValue();
		 * serviceInterfaceLog.setId(v_key);
		 * return effect;
		 */
	}

	@Override
	public int updateServiceInterfaceLog(ServiceInterfaceLog serviceInterfaceLog) {
		String sql = "{call p_service_interface_log_update(?,?,?)}";

		return getJdbcTemplate().update(sql, new Object[] { serviceInterfaceLog.getId(), serviceInterfaceLog.getStatusCode(), serviceInterfaceLog.getOutMsg() });
	}

	@Override
	public int updateServiceInterfaceSDK(long id, String sdkFile) {
		// TODO Auto-generated method stub
		String sql = "update t_service_interface set sdk_file=? where id=?";
		return getJdbcTemplate().update(sql, new Object[] { sdkFile, id });
	}

	@Override
	public int[] addRoleRequest(final long roleId, final String[] requestIds) {

		String sql = "insert into t_role_request_command(role_id,request_command_id) values(?,?)";

		final int size = requestIds.length;

		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, roleId);
				ps.setObject(2, requestIds[i]);

			}

			@Override
			public int getBatchSize() {
				return size;
			}
		};

		return getJdbcTemplate().batchUpdate(sql, batchSetter);

	}

	@Override
	public AppInfo findAppInfo(int id) {
		String sql = "select appinfo.*,apppay.pay_merchant_id  pay_merchant_id,apppay.pay_merchant_userid pay_merchant_userid,apppay.pay_notify_url pay_notify_url,apppay.pay_merchant_key  pay_merchant_key from wx_t_appinfo appinfo left outer join wx_T_appinfo_pay apppay on appinfo.id=apppay.wx_appinfo_id where appinfo.id=?";

		return queryForSingle(sql, new Object[] { id }, new RowMapperFactory.AppInfoRowMapper());
	}

	@Override
	public List<AppInfo> findAppInfo() {
		String sql = "select appinfo.*,apppay.pay_merchant_id pay_merchant_id,apppay.pay_merchant_userid pay_merchant_userid,apppay.pay_notify_url pay_notify_url,apppay.pay_merchant_key  pay_merchant_key from wx_t_appinfo appinfo left outer join wx_T_appinfo_pay apppay on appinfo.id=apppay.wx_appinfo_id where  start_refresh=1";

		return getJdbcTemplate().query(sql, new RowMapperFactory.AppInfoRowMapper());
	}

	@Override
	public List<WXAppInfoManager> findWXAppInfoManagerForCanReceive(int appinfo_id) {
		String sql = "select * from wx_t_appinfo_manager where is_auth=1 and receive_flag=1 and appinfo_id=?";

		return getJdbcTemplate().query(sql, new Object[] { appinfo_id }, new RowMapperFactory.WXAppInfoManagerRowMapper());

	}

	@Override
	public User findUser(String account) {

		String sql = "select * from t_user where account=?";

		return queryForSingle(sql, new Object[] { account }, new RowMapperFactory.UserRowMapper());
	}

	@Override
	public TemplateMsg findTemplateMsg(String templateCode) {
		String sql = "select * from wae_t_templatemsg where template_code=?";

		return queryForSingle(sql, new Object[] { templateCode }, new RowMapperFactory.TemplateMsgRowMapper());
	}

	@Override
	public List<WXCommand> loadWXCommand() {
		String sql = "select * from wx_t_command";

		return getJdbcTemplate().query(sql, new RowMapperFactory.WXCommandRowMapper());

	}

	@Override
	public int addUser(User user) {
		String sql = "insert into t_user(name,account,password,phone,role_id) values(?,?,?,?,?)";
		return getJdbcTemplate().update(sql, new Object[] { user.getName(), user.getAccount(), user.getPassword(), user.getPhone(),user.getRoleId() });
	}

	@Override
	public List<CorpAdminGroup> findCorpAdminGroup() {
		String sql = "select ag.*,agt.code admingroup_type_code,corpinfo.corp_id,corpinfo.name corpinfo_name  from wx_t_corp_admingroup ag join wx_t_corp_admingroup_type agt on ag.admingroup_type_id=agt.id join wx_t_corpinfo corpinfo on ag.corpinfo_id=corpinfo.id WHERE corpinfo.start_refresh=1";
		return getJdbcTemplate().query(sql, new RowMapperFactory.CorpAdminGroupRowMapper());
	}

	@Override
	public CorpInfo findCorpInfo(int corpInfoId) {
		String sql = "select * from wx_t_corpinfo where id=?";
		return queryForSingle(sql, new Object[] { corpInfoId }, new RowMapperFactory.CorpInfoRowMapper());
	}

	@Override
	public List<PropertiesTemplateItem> findPropertiesTemplate(String propertiesTemplateTypeCode) {

		String sql = "select pt.* from wae_t_properties_template pt  JOIN wae_t_properties_template_type ptt ON  pt.properties_template_type_id = ptt.id where ptt.code=?";
		return getJdbcTemplate().query(sql, new Object[] { propertiesTemplateTypeCode }, new RowMapperFactory.PropertiesTemplateItemRowMapper());
	}

	@Override
	public OAFlowBase addOAFlowBase(OAFlowBase oAFlowBase) {
		final String getId_sql = "insert into oa_t_flow_base(content,create_time,complete_time,initiator_userid) values(?,?,?,?)";

		final Object[] param = new Object[] { oAFlowBase.getContent(), oAFlowBase.getCreate_time(), oAFlowBase.getComplete_time(), oAFlowBase.getInitiator_userid() };

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				int k = 0;
				PreparedStatement ps = con.prepareStatement(getId_sql, Statement.RETURN_GENERATED_KEYS);

				for (; k < param.length; k++) {
					ps.setObject(k + 1, param[k]);
				}
				return ps;
			}
		}, keyHolder);

		int v_key = keyHolder.getKey().intValue();

		String business_key = Common.formatDate(new Date(), "yyyyMMddHHmmss") + ":" + v_key;

		oAFlowBase.setBusiness_key(business_key);

		String update_sql = "update oa_t_flow_base set business_key= ? where id=?";

		getJdbcTemplate().update(update_sql, new Object[] { business_key, v_key });

		return oAFlowBase;
	}

	@Override
	public int addOAFlowBaseAttach(String business_key, String media_id) {
		String sql = "insert into oa_t_flow_base_attach (business_key,media_id) values(?,?) ";
		return getJdbcTemplate().update(sql, new Object[] { business_key, media_id });
	}

	@Override
	public int addOAFlowTaskRecord(OAFlowTaskRecord taskRecord) {
		String sql = "insert into oa_t_taskrecord (business_key,task_id,content,user_id,task_result) values(?,?,?,?,?) ";
		return getJdbcTemplate().update(sql, new Object[] { taskRecord.getBusiness_key(), taskRecord.getTask_id(), taskRecord.getContent(), taskRecord.getUser_id(), taskRecord.getTask_result() });
	}

	@Override
	public OAFlowBase findOAFlowBase(String business_key) {
		String sql = "select * from oa_t_flow_base where business_key=?";
		return queryForSingle(sql, new Object[] { business_key }, new RowMapperFactory.OAFlowBaseRowMapper());
	}

	@Override
	public List<OAFlowItem> findFlows() {
		String sql = "select t1.*,t2.name,t2.remark,t2.process_key from oa_t_flows t1 left join oa_t_flow_type t2 on t1.flow_type_id = t2.id";
		return getJdbcTemplate().query(sql, new RowMapperFactory.OAFlowItemRowMapper());
	}

	@Override
	public void addMenuToApp(int menu_id, int product_id, String user_id) {
		String sql = "insert into wae_app_t_product_user(menu_id,product_id,user_id) values(?,?,?)";
		getJdbcTemplate().update(sql, menu_id, product_id, user_id);
	}

	@Override
	public String bleGeneratorNewEquipmentCode() {
		String sql = "select max(equipment_code) equipment_code from ble_t_equipment";
		String equipment_code = queryForObject(sql, String.class);
		if (!StringUtils.isEmpty(equipment_code)) {

		} else {
			equipment_code = "1000000";
		}
		Integer v = Integer.valueOf(equipment_code) + 1;

		return v.toString();
	}

	@Override
	public String bleGeneratorNewMajor() {
		String sql = "select max(major) major from ble_t_equipment";
		String major = queryForObject(sql, String.class);
		if (!StringUtils.isEmpty(major)) {

		} else {
			major = "10000";
		}

		Integer v = Integer.valueOf(major) + 1;
		return v.toString();
	}

	@Override
	public String bleGeneratorNewMinor() {
		String sql = "select max(minor) minor from ble_t_equipment";
		String minor = queryForObject(sql, String.class);
		if (!StringUtils.isEmpty(minor)) {

		} else {
			minor = "1000";
		}
		Integer v = Integer.valueOf(minor) + 1;
		return v.toString();
	}

	@Override
	public int addBleEquipment(BleEquipment item) {
		String sql = "insert into ble_t_equipment(name,equipment_code,uuid,major,minor,loc_address,create_time,remark) values(?,?,?,?,?,?,?,?)";

		return getJdbcTemplate().update(sql,
				new Object[] { item.getName(), item.getEquipmentCode(), item.getUuid(), item.getMajor(), item.getMinor(), item.getLocAddress(), new Date(), item.getRemark() });

	}

	@Override
	public boolean isBleEquipmentExists(int currentId, String uuid, String major, String minor) {
		String sql = "select count(1) cnt from ble_t_equipment where uuid=? and major=? and minor=? and id<>?";

		int cnt = queryForInt(sql, new Object[] { uuid, major, minor, currentId });
		if (cnt > 0)
			return true;
		return false;
	}

	@Override
	public List<Chart> findChartByUserId(String userId) {
		String sql = "SELECT c.*,ch.user_id FROM t_chart_user ch JOIN t_chart c ON ch.chart_id=c.id where ch.user_id=?";
		return getJdbcTemplate().query(sql, new Object[] { userId }, new RowMapperFactory.ChartRowMapper());
	}

	@Override
	public void deleteMenuFromApp(int product_id, String user_id) {
		String sql = "delete from wae_app_t_product_user where user_id = ? and product_id = ?";
		getJdbcTemplate().update(sql, user_id, product_id);

	}

	@Override
	public void addChartToUser(int char_id, String user_id) {
		String sql = "insert into t_chart_user(chart_id,user_id) values(?,?)";
		getJdbcTemplate().update(sql, char_id, user_id);
	}

	@Override
	public void deleteChartFromUser(String user_id) {
		String sql = "delete from t_chart_user where user_id = ? ";
		getJdbcTemplate().update(sql, user_id);
	}

	@Override
	public ScannerCode findScannerCode(String commandName) {
		logger.info("====CommonDao.findScannerCode:commandName" + commandName);
		String sql = "select * from wae_t_scannercode where command_name=?";
		ScannerCode sc = queryForSingle(sql, new Object[] { commandName }, new RowMapperFactory.ScannerCodeRowMapper());
		logger.info("====CommonDao.findScannerCode sc:" + sc);
		return sc;
	}

	@Override
	public int findFlowTaskRecord(OAFlowTaskRecord taskRecord) {
		String sql = "select count(*) from oa_t_taskrecord where task_id = ?";
		return queryForInt(sql, taskRecord.getTask_id());
	}

	@Override
	public int findFlowTaskRecordByUserId(OAFlowTaskRecord taskRecord) {
		String sql = "select count(*) from oa_t_taskrecord where task_id = ? and user_id = ?";
		return queryForInt(sql, taskRecord.getTask_id(), taskRecord.getUser_id());
	}

	@Override
	public List<BleEquipment> findBleEquipmentByCode(String code) {
		String sql = "select * from  ble_t_equipment where equipment_code = ?";
		return getJdbcTemplate().query(sql, new Object[] { code }, new RowMapperFactory.BleEquipmentRowMapper());
	}

	@Override
	public List<Map<String, Object>> findWorkorderProgress() {
		String sql = "select * from  v_act_business_key_progress";
		return getJdbcTemplate().queryForList(sql);
	}

	@Override
	public AppInfo findAppInfoByWXAppId(String appid) {
		String sql="select * from wx_t_appinfo where appid=?";
		return queryForSingle(sql,new Object[]{appid},new RowMapperFactory.AppInfoRowMapper());
	}

	@Override
	public List<Map<String, Object>> findWxMenus(int wx_appinfo_id, int parentId) {

		String sql="select * from wx_t_menus where wx_appinfo_id=? and p_id=? order by sort asc";
		return getJdbcTemplate().queryForList(sql,new Object[]{wx_appinfo_id,parentId});
	}

	@Override
	public String findWxMenuMediaIdByEventKey(int wx_appinfo_id, String eventKey) {
		String sql="select media_id from wx_t_menus where wx_appinfo_id=? and key_id=?";
		return getJdbcTemplate().queryForObject(sql,new Object[]{wx_appinfo_id,eventKey},String.class);
	}

	@Override
	public Map<String, Object> findSmsConfig(String mch_code) {
		String sql="select * from wae_third_t_smsconfig where mch_code=?";
		return queryForMap(sql, new Object[]{mch_code});
	}

	@Override
	public String saveSmsMsg(Map<String, Object> msg) {
		String id=UUID.randomUUID().toString().replace("-","");
		
		String sql="insert into wae_third_t_smsmsg(id,module_id,mobile,msg_content,valid_code,create_time,valid_time,is_valid) values(?,?,?,?,?,?,?,?)";
		
		
		getJdbcTemplate().update(sql,new Object[]{
				id,
				"",
				msg.get("mobille"),
				msg.get("msg_content"),
				msg.get("valid_code"),
				new Date(),
				msg.get("valid_time"),
				0
				
				
		});
		
		
		return id;
		
	}

	@Override
	public String validSmsValidCode(String id,String valid_code) {
		String msg="";
		String sql="select * from wae_third_t_smsmsg where id=? and valid_code=?";
		Map<String,Object> item= queryForMap(sql,new Object[]{id,valid_code});
		
		if(item==null){
			msg="验证码错误";
		}else{
			
			Date create_time=Common.parseDateFull(item.get("create_time").toString());
			long valid_duration=Long.parseLong(item.get("valid_time").toString());
			
			Date now=new Date();
			
			int x=(int) ((now.getTime()-create_time.getTime())-valid_duration*1000);
			
			if(x<=0){
				//验证码验证通过
				
			}else{
				msg="验证码失效了";
				
			}
			
		}
		
		return msg;
	}

	@Override
	public int findPhoneNumberHasMchNumber(String phone) {
		String sql="select count(*) cnt from wae_third_t_smsmsg where mobile=?";
		return getJdbcTemplate().queryForObject(sql, new Object[]{phone},Integer.class);
		
	
	}

	@Override
	public List<User> findUserByRoleId(int roleId) {
		String sql = "select * from  t_user where role_id = ?";
		return getJdbcTemplate().query(sql, new Object[] { roleId }, new RowMapperFactory.UserRowMapper());
	
	}

	@Override
	public int updateWXMenuMediaLink(int menu_id, String media_id) {
		String sql="update wx_t_menus set media_id=? where id=?";
		return getJdbcTemplate().update(sql,new Object[]{media_id,menu_id});
	}

	@Override
	public List<Map<String, Object>> findWxApp(String appid) {
		String sql="select * from wx_t_app where wx_appid=?";
		return queryForList(sql, new Object[]{appid});
	}

	@Override
	public int updateWxAppInfoTokenStatus(String appid, String status) {
		String sql="update wx_t_appinfo set token_status=? where appid=?";
		return getJdbcTemplate().update(sql,new Object[]{status,appid});
	}

	@Override
	public int updateWxCorpTokenStatus(String appid, String status) {
		String sql="update wx_t_corpinfo set token_status=? where corp_id=?";
		return getJdbcTemplate().update(sql,new Object[]{status,appid});
	}

	
	
}
