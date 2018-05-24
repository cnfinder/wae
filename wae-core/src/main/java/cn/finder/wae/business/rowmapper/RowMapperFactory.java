package cn.finder.wae.business.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.FileMsg;
import cn.finder.wae.business.domain.GridMenu;
import cn.finder.wae.business.domain.Log;
import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.NotificationInfo;
import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.RCInfo;
import cn.finder.wae.business.domain.RequestCommand;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.RoleMenu;
import cn.finder.wae.business.domain.RoleReqCommand;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowDataType;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.ShowtableConfigProcess;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.TableField;
import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.domain.UserConfig;
import cn.finder.wae.business.domain.UserSetting;
import cn.finder.wae.business.domain.ble.BleEquipment;
import cn.finder.wae.business.domain.chart.Chart;
import cn.finder.wae.business.domain.oaFlow.OAFlowBase;
import cn.finder.wae.business.domain.oaFlow.OAFlowItem;
import cn.finder.wae.business.domain.oaFlow.OAFlowType;
import cn.finder.wae.business.domain.properties.PropertiesItem;
import cn.finder.wae.business.domain.properties.PropertiesTemplateItem;
import cn.finder.wae.business.domain.report.TReport;
import cn.finder.wae.business.domain.report.TReportField;
import cn.finder.wae.business.domain.scannercode.ScannerCode;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.domain.wx.WXAppInfoManager;
import cn.finder.wae.business.domain.wx.WXCommand;
import cn.finder.wae.business.domain.wx.WXMenu;
import cn.finder.wae.business.domain.wx.corp.CorpAdminGroup;
import cn.finder.wae.business.domain.wx.corp.CorpInfo;

public class RowMapperFactory {

	private final static Logger logger = Logger.getLogger(RowMapperFactory.class);

	public static class UserRowMapper implements org.springframework.jdbc.core.RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int i) throws SQLException {
			User item = new User();

			item.setId(rs.getLong("id"));
			item.setAccount(rs.getString("account"));
			item.setAddress(rs.getString("address"));
			item.setAge(rs.getInt("age"));
			item.setCreateDate(rs.getTimestamp("create_date"));
			item.setUpdateDate(rs.getTimestamp("update_date"));
			item.setEmail(rs.getString("email"));
			item.setGender(rs.getInt("gender"));
			item.setName(rs.getString("name"));
			item.setPhone(rs.getString("phone"));
			item.setRoleId(rs.getLong("role_id"));
			item.setPassword(rs.getString("password"));
			item.setBirthday(rs.getTimestamp("birthday"));
			item.setDptId(rs.getLong("department_id"));
			try {
				item.setRole_code(rs.getString("role_code"));
			} catch (Exception e) {
			}
			return item;
		}

	}

	public static class SysConfigRowMapper implements org.springframework.jdbc.core.RowMapper<SysConfig> {
		@Override
		public SysConfig mapRow(ResultSet rs, int i) throws SQLException {
			SysConfig item = new SysConfig();
			item.setId(rs.getLong("ID"));
			item.setName(rs.getString("NAME"));
			item.setValue(rs.getString("VALUE"));
			item.setCnName(rs.getString("CN_NAME"));
			item.setGroupId(rs.getString("groupid"));
			item.setDesc(rs.getString("REMARK"));
			item.setIsSecret(rs.getInt("MD5_CRYPT"));
			return item;
		}
	}

	public static class UserConfigRowMapper implements org.springframework.jdbc.core.RowMapper<UserConfig> {
		@Override
		public UserConfig mapRow(ResultSet rs, int i) throws SQLException {
			UserConfig item = new UserConfig();
			item.setId(rs.getLong("ID"));
			item.setName(rs.getString("NAME"));
			item.setValue(rs.getString("VALUE"));
			item.setCnName(rs.getString("CN_NAME"));
			item.setGroupName(rs.getString("group_name"));
			item.setDesc(rs.getString("REMARK"));
			item.setIsSecret(rs.getInt("MD5_CRYPT"));
			return item;
		}
	}

	public static class ShowTableConfigRowMapper implements org.springframework.jdbc.core.RowMapper<ShowTableConfig> {
		@Override
		public ShowTableConfig mapRow(ResultSet rs, int i) throws SQLException {
			ShowTableConfig item = new ShowTableConfig();
			item.setId(rs.getLong("ID"));
			item.setName(rs.getString("name"));
			item.setShowTableName(rs.getString("showtable_name"));
			item.setShowType(rs.getInt("show_type"));
			item.setSqlConfig(rs.getString("sql_config"));
			item.setPageSize(rs.getInt("page_size"));
			item.setProcessCommand(rs.getString("process_command"));
			item.setCreateDate(rs.getTimestamp("create_date"));
			item.setUpdateDate(rs.getTimestamp("update_date"));

			item.setGridrowFrontCmd(rs.getString("gridrow_front_cmd"));
			item.setGridrowBackCmd(rs.getString("gridrow_back_cmd"));
			item.setResultName(rs.getString("result_name"));
			item.setChildCmd(rs.getString("child_cmd"));
			item.setChildShowtableConfigId(rs.getLong("child_showtable_config_id"));
			item.setAddShowtableConfigId(rs.getLong("add_showtable_config_id"));
			item.setEditShowtableConfigId(rs.getLong("edit_showtable_config_id"));
			item.setSearchShowtableConfigId(rs.getLong("search_showtable_config_id"));
			item.setTargetDs(rs.getLong("target_ds"));
			item.setDeleteShowtableConfigId(rs.getInt("delete_showtable_config_id"));
			item.setIsMultiselect(rs.getInt("is_multiselect"));
			item.setIsFirstQueryData(rs.getInt("is_first_querydata"));
			item.setIsPager(rs.getInt("is_pager"));
			try {
				item.setExtProps(rs.getString("ext_prop"));
			} catch (Exception e) {

			}

			return item;
		}
	}

	public static class ShowDataConfigRowMapper implements org.springframework.jdbc.core.RowMapper<ShowDataConfig> {
		@Override
		public ShowDataConfig mapRow(ResultSet rs, int i) throws SQLException {
			ShowDataConfig item = new ShowDataConfig();
			item.setId(rs.getLong("ID"));
			item.setShowTableName(rs.getString("showtable_name"));

			item.setFieldName(rs.getString("field_name"));
			item.setFieldNameAlias(rs.getString("field_name_alias"));
			item.setEditable(rs.getInt("editable"));
			item.setShowType(rs.getInt("show_type"));

			item.setSort(rs.getInt("sort"));

			item.setParentSelectSql(rs.getString("parent_select_sql"));
			item.setParentTableName(rs.getString("parent_table_name"));
			item.setParentTableKey(rs.getString("parent_table_key"));
			item.setParentShowField(rs.getString("parent_show_field"));
			item.setWidth(rs.getInt("width"));
			item.setStatistics(rs.getString("statistic"));
			item.setCreateDate(rs.getTimestamp("create_date"));
			item.setUpdateDate(rs.getTimestamp("update_date"));

			item.setIsHide(rs.getInt("is_hide"));
			item.setIsLoad(rs.getInt("is_load"));
			item.setAllowSort(rs.getInt("allow_sort"));
			item.setDataType(rs.getInt("data_type"));

			item.setGroupName(rs.getString("group_name"));
			item.setParentGroupName(rs.getString("p_group_name"));
			item.setFieldNav(rs.getString("field_nav"));
			item.setFormat(rs.getString("format"));
			item.setItemRenderEvent(rs.getString("item_render_event"));
			item.setIsShowSearch(rs.getInt("is_show_search"));

			item.setFieldTableName(rs.getString("field_tablename"));

			item.setDefaultValue(rs.getString("default_value"));

			item.setTipText(rs.getString("tip_text"));
			item.setValidateTip(rs.getString("validate_tip"));
			item.setValidateRule(rs.getString("validate_rule"));

			item.setIsCrypt(rs.getInt("is_crypt"));
			item.setHeight(rs.getInt("height"));

			item.setIsPrimaryKey(rs.getInt("is_primarykey"));
			item.setParentControlId(rs.getString("parent_controlId"));

			item.setIsMergeColumn(rs.getInt("is_merge_column"));
			ShowDataType showDataType = new ShowDataType();
			item.setShowDataType(showDataType);
			try {
				showDataType.setCode(rs.getString("showdatatype_code"));
				showDataType.setName(rs.getString("showdatatype_name"));
				showDataType.setGroupName(rs.getString("showdatatype_group_name"));
			} catch (Exception e) {

			}

			item.setExtProperties(rs.getString("ext_properties"));

			item.setLockField(rs.getInt("lock_field"));
			return item;
		}
	}

	public static class MenuRowMapper implements org.springframework.jdbc.core.RowMapper<Menu> {

		@Override
		public Menu mapRow(ResultSet rs, int i) throws SQLException {
			Menu m = new Menu();
			m.setId(rs.getLong("id"));
			m.setCommand(rs.getString("command"));
			m.setCreateDate(rs.getTimestamp("create_date"));
			m.setIsEnable(rs.getInt("is_enable"));
			m.setLevel(rs.getInt("level"));
			m.setName(rs.getString("name"));
			m.setParentId(rs.getLong("parent_id"));
			m.setUpdateDate(rs.getTimestamp("update_date"));
			m.setType(rs.getLong("type"));
			m.setStyle(rs.getString("style"));
			m.setStyleHover(rs.getString("style_hover"));
			m.setIconPosition(rs.getString("icon_position"));
			m.setSort(rs.getInt("sort"));
			return m;
		}

	}

	public static class RoleRowMapper implements org.springframework.jdbc.core.RowMapper<Role> {

		@Override
		public Role mapRow(ResultSet rs, int i) throws SQLException {
			Role r = new Role();
			r.setCreateDate(rs.getTimestamp("create_date"));
			r.setId(rs.getLong("id"));
			r.setName(rs.getString("name"));
			r.setUpdateDate(rs.getTimestamp("update_date"));
			r.setParentId(rs.getLong("parent_id"));
			r.setRoleCode(rs.getString("role_code"));
			return r;
		}

	}

	public static class RoleMenuRowMapper implements org.springframework.jdbc.core.RowMapper<RoleMenu> {

		@Override
		public RoleMenu mapRow(ResultSet rs, int i) throws SQLException {
			RoleMenu rm = new RoleMenu();
			rm.setCreateDate(rs.getTimestamp("create_date"));
			rm.setId(rs.getLong("id"));
			rm.setMenuId(rs.getLong("menu_id"));
			rm.setRoleId(rs.getLong("role_id"));
			rm.setUpdateDate(rs.getTimestamp("update_date"));

			Role role = new Role();
			role.setId(rm.getRoleId());

			Menu m = new Menu();
			m.setId(rm.getMenuId());
			m.setCommand(rs.getString("command"));
			m.setCreateDate(rs.getTimestamp("create_date"));
			m.setIsEnable(rs.getInt("is_enable"));
			m.setLevel(rs.getInt("level"));
			m.setName(rs.getString("name"));
			m.setParentId(rs.getLong("parent_id"));
			m.setUpdateDate(rs.getTimestamp("update_date"));
			m.setType(rs.getLong("type"));
			m.setStyle(rs.getString("style"));
			m.setStyleHover(rs.getString("style_hover"));
			m.setIconPosition(rs.getString("icon_position"));

			rm.setRole(role);
			rm.setMenu(m);

			return rm;
		}

	}

	public static class RequestCommandRowMapper implements org.springframework.jdbc.core.RowMapper<RequestCommand> {

		@Override
		public RequestCommand mapRow(ResultSet rs, int i) throws SQLException {
			RequestCommand rc = new RequestCommand();
			rc.setCommand(rs.getString("command"));
			rc.setId(rs.getLong("id"));
			rc.setName(rs.getString("name"));
			rc.setRemark(rs.getString("remark"));
			return rc;
		}

	}

	public static class GridMenuRowMapper implements org.springframework.jdbc.core.RowMapper<GridMenu> {

		@Override
		public GridMenu mapRow(ResultSet rs, int i) throws SQLException {
			GridMenu item = new GridMenu();
			item.setId(rs.getLong("id"));
			item.setName(rs.getString("name"));
			item.setCommand(rs.getString("command"));

			item.setMenuType(rs.getInt("menu_type"));
			item.setSort(rs.getInt("sort"));
			item.setCreateDate(rs.getTimestamp("create_date"));
			item.setUpdateDate(rs.getTimestamp("update_date"));
			item.setShowTableConfigId(rs.getLong("showtable_config_id"));
			item.setIsAuth(rs.getInt("is_auth"));
			item.setIconCls(rs.getString("icon_cls"));
			item.setColumnType(rs.getInt("column_type"));
			item.setRemark(rs.getString("remark"));
			item.setWidth(rs.getInt("width"));
			item.setHeight(rs.getInt("height"));
			item.setExtSetting(rs.getString("ext_setting"));
			item.setCellCmdRender(rs.getString("cell_cmd_render"));
			return item;
		}

	}

	public static class ConstantsRowMapper implements org.springframework.jdbc.core.RowMapper<Constants> {
		@Override
		public Constants mapRow(ResultSet rs, int i) throws SQLException {
			Constants item = new Constants();
			item.setId(rs.getLong("ID"));
			item.setName(rs.getString("NAME"));
			item.setValue(rs.getString("VALUE"));
			item.setNameAlias(rs.getString("name_alias"));
			item.setGroupName(rs.getString("group_name"));
			item.setConstantsGroupId(rs.getLong("constants_group_id"));
			return item;
		}
	}

	public static class TableFieldRowMapper implements org.springframework.jdbc.core.RowMapper<TableField> {

		@Override
		public TableField mapRow(ResultSet rs, int index) throws SQLException {
			TableField entitybean = new TableField();
			entitybean.setTableName(rs.getString("table_name"));
			entitybean.setColumnName(rs.getString("column_name"));
			entitybean.setDataType(rs.getString("data_type"));
			entitybean.setDefaultValue(rs.getString("column_default"));
			// entitybean.setIsPrimaryKey(rs.getInt("isPrimaryKey"));
			entitybean.setColumnKey(rs.getString("column_key"));
			return entitybean;
		}

	}

	public static class FileMsgRowMapper implements org.springframework.jdbc.core.RowMapper<FileMsg> {

		@Override
		public FileMsg mapRow(ResultSet rs, int index) throws SQLException {
			FileMsg entitybean = new FileMsg();
			entitybean.setId(rs.getLong("id"));
			entitybean.setName(rs.getString("name"));
			entitybean.setFilePath(rs.getString("filePath"));
			entitybean.setFileType(rs.getString("fileType"));
			entitybean.setFileSize(rs.getLong("fileSize"));
			entitybean.setUploader(rs.getLong("uploader"));
			entitybean.setParentId(rs.getLong("parentId"));
			entitybean.setDownloadTimes(rs.getLong("downloadTimes"));
			entitybean.setCreateDate(rs.getDate("createDate"));
			entitybean.setUpdateDate(rs.getDate("updateDate"));
			return entitybean;
		}
	}

	public static class DataImportConfigRowMapper implements org.springframework.jdbc.core.RowMapper<DataImportConfig> {

		@Override
		public DataImportConfig mapRow(ResultSet rs, int index) throws SQLException {
			DataImportConfig entitybean = new DataImportConfig();
			entitybean.setId(rs.getLong("id"));
			entitybean.setName(rs.getString("name"));
			entitybean.setProcessShowTableConfigId(rs.getLong("process_showtable_config_id"));
			entitybean.setSourceShowTableConfigId(rs.getLong("source_showtable_config_id"));
			entitybean.setDestShowTableConfigId(rs.getLong("dest_showtable_config_id"));
			entitybean.setFrequency(rs.getLong("frequency"));
			entitybean.setRemark(rs.getString("remark"));
			entitybean.setIsRunning(rs.getInt("is_running"));
			entitybean.setIsBackUp(rs.getInt("is_back_up"));
			entitybean.setStartWithServer(rs.getInt("start_with_server"));
			entitybean.setStartTime(rs.getInt("start_time"));
			entitybean.setIsDelete(rs.getInt("is_delete"));
			entitybean.setIsImport(rs.getInt("is_import"));
			entitybean.setReportId(rs.getInt("report_id"));
			return entitybean;
		}
	}

	public static class LogRowMapper implements org.springframework.jdbc.core.RowMapper<Log> {

		@Override
		public Log mapRow(ResultSet rs, int index) throws SQLException {
			Log entitybean = new Log();
			entitybean.setId(rs.getInt("id"));
			entitybean.setUser_id(rs.getInt("user_id"));
			entitybean.setExit_time(rs.getDate("exit_time"));
			entitybean.setLogDate(rs.getDate("logdate"));
			entitybean.setLevelName(rs.getString("levelname"));
			entitybean.setLineNumber(rs.getString("linenumber"));
			entitybean.setMethodName(rs.getString("methodname"));
			entitybean.setClassName(rs.getString("classname"));
			entitybean.setThreadName(rs.getString("threadname"));
			entitybean.setMessage(rs.getString("message"));
			entitybean.setiP(rs.getString("ip"));
			entitybean.setUserName(rs.getString("user_name"));
			entitybean.setLogtype(rs.getInt("logtype"));
			entitybean.setSessionId(rs.getString("session_id"));
			return entitybean;
		}
	}

	public static class TaskPlanRowMapper implements org.springframework.jdbc.core.RowMapper<TaskPlan> {

		@Override
		public TaskPlan mapRow(ResultSet rs, int index) throws SQLException {

			TaskPlan item = new TaskPlan();

			item.setId(rs.getLong("id"));

			item.setName(rs.getString("name"));

			item.setShowtableConfigId(rs.getLong("showtable_config_id"));

			item.setDuration(rs.getLong("duration"));

			item.setStatus(rs.getInt("status"));

			java.sql.Date startTime = rs.getDate("start_time");

			if (startTime != null)
				item.setStartTime(new Date(startTime.getTime()));

			item.setParams(rs.getString("params"));

			if (!StringUtils.isEmpty(item.getParams())) {
				String[] ps = item.getParams().split(";");

				for (int i = 0; i < ps.length; i++) {

					String[] kv = ps[i].split("=");

					item.getMapPara().put(kv[0], kv[1]);

				}
			}

			item.setTimes(rs.getInt("times"));
			item.setProcessClass(rs.getString("process_class"));

			item.setRemark(rs.getString("remark"));

			item.setIsUpdated(rs.getInt("is_updated"));

			item.setStartWithServer(rs.getInt("start_with_server"));

			item.setIsThreadQueue(rs.getInt("is_thread_queue"));
			return item;

		}
	}

	public static class NotificationInfoRowMapper implements org.springframework.jdbc.core.RowMapper<NotificationInfo> {

		@Override
		public NotificationInfo mapRow(ResultSet rs, int index) throws SQLException {
			NotificationInfo item = new NotificationInfo();

			item.setId(rs.getLong("id"));
			item.setFromUser(rs.getString("from_user"));
			item.setToUser(rs.getString("to_user"));
			item.setIsRead(rs.getInt("is_read"));
			item.setReadDate(new java.util.Date(rs.getTimestamp("read_date").getTime()));
			item.setTitle(rs.getString("title"));
			item.setNotificationMsg(rs.getString("notification_msg"));
			item.setNotificationType(rs.getLong("notification_type"));

			return item;
		}
	}

	public static class RCInfoRowMapper implements org.springframework.jdbc.core.RowMapper<RCInfo> {
		@Override
		public RCInfo mapRow(ResultSet rs, int i) throws SQLException {
			RCInfo item = new RCInfo();
			item.setId(rs.getInt("ID"));
			item.setRcUserId(rs.getString("rc_userid"));
			item.setRcPwd(rs.getString("rc_pwd"));
			item.setTargetCode(rs.getString("target_code"));
			item.setTargetIp(rs.getString("target_ip"));
			item.setCreateDate(rs.getTimestamp("create_date"));
			item.setUpdateDate(rs.getTimestamp("update_date"));

			return item;
		}
	}

	public static class UserSettingRowMapper implements org.springframework.jdbc.core.RowMapper<UserSetting> {
		@Override
		public UserSetting mapRow(ResultSet rs, int i) throws SQLException {
			UserSetting item = new UserSetting();
			item.setId(rs.getInt("ID"));
			item.setIsLeftCollapse(rs.getInt("is_left_collapse"));
			item.setUserName(rs.getString("user_name"));
			item.setManageIndexPage(rs.getString("manage_index_page"));
			item.setBrowserMax(rs.getInt("browser_max"));
			item.setFirstMenuPage(rs.getString("first_menu_page"));
			item.setExtSetting(rs.getString("ext_setting"));

			return item;
		}
	}

	public static class ReportFieldRowMapper implements org.springframework.jdbc.core.RowMapper<TReportField> {
		@Override
		public TReportField mapRow(ResultSet rs, int i) throws SQLException {
			TReportField item = new TReportField();
			item.setFieldCode(rs.getString("field_code"));
			item.setFieldName(rs.getString("field_name"));
			item.setId(rs.getInt("id"));
			item.setState(rs.getShort("state"));
			item.setReport_id(rs.getInt("report_id"));
			return item;
		}
	}

	public static class ReportRowMapper implements org.springframework.jdbc.core.RowMapper<TReport> {
		@Override
		public TReport mapRow(ResultSet rs, int i) throws SQLException {
			TReport item = new TReport();
			item.setId(rs.getInt("id"));
			item.setState(rs.getShort("state"));
			item.setNumber(rs.getInt("number"));
			item.setRemark(rs.getString("remark"));
			item.setUrl(rs.getString("url"));
			item.setName(rs.getString("name"));
			item.setType(rs.getShort("type"));
			item.setKind(rs.getShort("kind"));
			item.setLastUpdate(rs.getTimestamp("last_update"));
			item.setShowtableConfigId(rs.getInt("showtable_config_id"));
			item.setLimitField(rs.getString("limit_field"));
			return item;
		}
	}

	public static class ServiceInterfaceRowMapper implements org.springframework.jdbc.core.RowMapper<ServiceInterface> {

		@Override
		public ServiceInterface mapRow(ResultSet rs, int i) throws SQLException {
			ServiceInterface item = new ServiceInterface();

			item.setId(rs.getInt("Id"));
			item.setInterfaceName(rs.getString("interface_name"));
			item.setInterfaceNameCn(rs.getString("interface_name_cn"));
			item.setShowtableConfigId(rs.getLong("showtableconfig_id"));
			item.setIsNeedAuth(rs.getInt("is_need_auth"));
			item.setVersion(rs.getString("version"));
			item.setEnabled(rs.getInt("enabled"));
			item.setRemark(rs.getString("remark"));
			item.setGroupName(rs.getString("group_name"));
			item.setEnableLog(rs.getInt("enable_log"));
			return item;
		}

	}

	public static class ShowtableConfigProcessRowMapper implements org.springframework.jdbc.core.RowMapper<ShowtableConfigProcess> {
		@Override
		public ShowtableConfigProcess mapRow(ResultSet rs, int i) throws SQLException {
			ShowtableConfigProcess item = new ShowtableConfigProcess();
			item.setShowtableConfigId(rs.getLong("showtable_config_id"));
			item.setProcessKey(rs.getString("process_key"));
			item.setBusinessKey(rs.getString("business_key"));
			item.setRemark(rs.getString("remark"));
			return item;
		}
	}

	public static class AppInfoRowMapper implements org.springframework.jdbc.core.RowMapper<AppInfo> {
		@Override
		public AppInfo mapRow(ResultSet rs, int i) throws SQLException {
			logger.info("====AppInfoRowMapper:");
			AppInfo item = new AppInfo();
			item.setId(rs.getInt("id"));
			logger.info("item.setId(rs.getInt(\"id\")):"+item.getId());
			item.setName(rs.getString("name"));
			item.setAppid(rs.getString("appid"));
			item.setAppSecret(rs.getString("appsecret"));
			item.setOpenid(rs.getString("openid"));
			item.setGrantType(rs.getString("grant_type"));
			item.setHeadimgurl(rs.getString("head_imgurl"));
			
			item.setType(rs.getInt("type"));
			
			AppInfo.MerchantInfo merchantInfo=new AppInfo.MerchantInfo();
			item.setMerchantInfo(merchantInfo);
			try{
				merchantInfo.setMerchant_id(rs.getString("pay_merchant_id"));
				merchantInfo.setMerchant_userid(rs.getString("pay_merchant_userid"));
				merchantInfo.setMerchant_key(rs.getString("pay_merchant_key"));
				merchantInfo.setNotify_url(rs.getString("pay_notify_url"));
				logger.info("=============merchantInfo:"+merchantInfo.toString());
			}
			catch(Exception e){
				logger.warn(e);
			}
			
			return item;
		}
	}

	public static class WXMenus implements org.springframework.jdbc.core.RowMapper<WXMenu> {
		@Override
		public WXMenu mapRow(ResultSet rs, int index) throws SQLException {
			WXMenu menu = new WXMenu();
			menu.setId(rs.getInt("id"));
			menu.setName(rs.getString("name"));
			menu.setType(rs.getString("type"));
			menu.setUrl(rs.getString("url"));
			menu.setKey(rs.getString("key_id"));
			return menu;
		}
	}

	public static class WXAppInfoManagerRowMapper implements org.springframework.jdbc.core.RowMapper<WXAppInfoManager> {
		@Override
		public WXAppInfoManager mapRow(ResultSet rs, int index) throws SQLException {
			WXAppInfoManager item = new WXAppInfoManager();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setOpenid(rs.getString("openid"));
			item.setReceiveFlag(rs.getInt("receive_flag"));

			item.setAppInfoId(rs.getInt("appinfo_id"));

			return item;
		}
	}

	public static class TemplateMsgRowMapper implements org.springframework.jdbc.core.RowMapper<TemplateMsg> {
		@Override
		public TemplateMsg mapRow(ResultSet rs, int index) throws SQLException {
			TemplateMsg item = new TemplateMsg();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));

			item.setTitle(rs.getString("title"));

			item.setTemplateCode(rs.getString("template_code"));
			item.setTemplateContent(rs.getString("template_content"));

			return item;
		}
	}

	public static class RoleReqCommandRowMapper implements org.springframework.jdbc.core.RowMapper<RoleReqCommand> {

		@Override
		public RoleReqCommand mapRow(ResultSet rs, int i) throws SQLException {
			RoleReqCommand item = new RoleReqCommand();
			item.setId(rs.getLong("id"));
			item.setRole_id(rs.getLong("role_id"));
			item.setRequest_command_id(rs.getLong("request_command_id"));
			item.setRequestCommandName(rs.getString("request_command_name"));
			item.setCommand(rs.getString("command"));

			return item;
		}

	}

	public static class WXCommandRowMapper implements org.springframework.jdbc.core.RowMapper<WXCommand> {

		@Override
		public WXCommand mapRow(ResultSet rs, int i) throws SQLException {
			WXCommand item = new WXCommand();
			item.setId(rs.getInt("id"));
			item.setCommandStr(rs.getString("command_str"));
			item.setName(rs.getString("name"));
			item.setMsgType(rs.getString("msg_type"));
			item.setMsgTemplateId(rs.getString("msg_template_id"));
			item.setUrl(rs.getString("url"));
			item.setPicUrl(rs.getString("pic_url"));
			item.setItemDesc(rs.getString("item_desc"));
			return item;
		}

	}

	public static class CorpAdminGroupRowMapper implements org.springframework.jdbc.core.RowMapper<CorpAdminGroup> {

		@Override
		public CorpAdminGroup mapRow(ResultSet rs, int i) throws SQLException {
			CorpAdminGroup item = new CorpAdminGroup();
			item.setCorpinfo_id(rs.getInt("corpinfo_id"));

			item.setAdminGroupTypeName(rs.getString("name"));

			item.setAdminGroupTypeCode(rs.getString("admingroup_type_code"));
			item.setCorp_id(rs.getString("corp_id"));
			item.setSecret(rs.getString("secret"));
			item.setCorpinfo_name(rs.getString("corpinfo_name"));
			return item;
		}

	}

	public static class CorpInfoRowMapper implements org.springframework.jdbc.core.RowMapper<CorpInfo> {

		@Override
		public CorpInfo mapRow(ResultSet rs, int i) throws SQLException {
			CorpInfo item = new CorpInfo();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setCorp_id(rs.getString("corp_id"));
			return item;
		}

	}

	public static class PropertiesItemRowMapper implements org.springframework.jdbc.core.RowMapper<PropertiesItem> {

		@Override
		public PropertiesItem mapRow(ResultSet rs, int i) throws SQLException {
			PropertiesItem item = new PropertiesItem();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setPropName(rs.getString("prop_name"));
			item.setPropValue(rs.getString("prop_value"));

			return item;
		}

	}

	public static class PropertiesTemplateItemRowMapper implements org.springframework.jdbc.core.RowMapper<PropertiesTemplateItem> {

		@Override
		public PropertiesTemplateItem mapRow(ResultSet rs, int i) throws SQLException {
			PropertiesTemplateItem item = new PropertiesTemplateItem();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setShow_type(rs.getInt("show_type"));
			item.setData_type(rs.getInt("data_type"));
			item.setProcess_command(rs.getString("process_command"));
			item.setData_showtableConfigId(rs.getInt("data_showtableConfigId"));
			item.setData_source_static(rs.getString("data_source_static"));
			item.setSort(rs.getInt("sort"));
			item.setProp_name(rs.getString("prop_name"));
			item.setIs_new_line(rs.getInt("is_new_line"));
			item.setIs_required(rs.getInt("is_required"));
			item.setProperties_template_type_id(rs.getInt("properties_template_type_id"));
			item.setCtrl_width(rs.getInt("ctrl_width"));
			item.setCtrl_height(rs.getInt("ctrl_height"));
			item.setDynamic_default_value(rs.getString("dynamic_default_value"));
			item.setStatic_default_value(rs.getString("static_default_value"));
			item.setIs_editable(rs.getInt("is_editable"));

			return item;
		}

	}

	public static class OAFlowBaseRowMapper implements org.springframework.jdbc.core.RowMapper<OAFlowBase> {

		@Override
		public OAFlowBase mapRow(ResultSet rs, int i) throws SQLException {
			OAFlowBase item = new OAFlowBase();

			item.setId(rs.getInt("id"));
			item.setContent(rs.getString("content"));
			item.setCreate_time(rs.getDate("create_time"));
			item.setBusiness_key(rs.getString("business_key"));
			item.setComplete_time(rs.getDate("complete_time"));
			item.setInitiator_userid(rs.getString("initiator_userid"));

			return item;
		}

	}

	public static class OAFlowItemRowMapper implements org.springframework.jdbc.core.RowMapper<OAFlowItem> {

		@Override
		public OAFlowItem mapRow(ResultSet rs, int i) throws SQLException {
			OAFlowItem item = new OAFlowItem();

			item.setId(rs.getInt("id"));
			item.setFlowName(rs.getString("flow_name"));
			item.setProcedefProcessKey(rs.getString("procdef_processkey"));
			item.setFlowDesc(rs.getString("flow_desc"));
			try {
				OAFlowType oaFlowType = new OAFlowType(rs.getInt("flow_type_id"), rs.getString("name"), rs.getString("remark"), rs.getString("process_key"));
				item.setOaFlowType(oaFlowType);
			} catch (Exception e) {
			}
			return item;
		}

	}

	public static class ChartRowMapper implements org.springframework.jdbc.core.RowMapper<Chart> {

		@Override
		public Chart mapRow(ResultSet rs, int i) throws SQLException {
			Chart item = new Chart();

			item.setId(rs.getInt("id"));

			item.setName(rs.getString("name"));
			item.setTitle(rs.getString("title"));
			item.setSub_title(rs.getString("sub_title"));
			item.setShowtable_config_id(rs.getLong("Showtable_config_id"));
			item.setShowtable_config_id_page(rs.getLong("showtable_config_id_page"));
			return item;
		}

	}

	public static class ScannerCodeRowMapper implements org.springframework.jdbc.core.RowMapper<ScannerCode> {

		@Override
		public ScannerCode mapRow(ResultSet rs, int i) throws SQLException {
			ScannerCode item = new ScannerCode();

			item.setId(rs.getInt("id"));

			item.setName(rs.getString("name"));
			item.setCommandName(rs.getString("command_name"));
			item.setShowtableConfigId(rs.getLong("Showtable_config_id"));
			item.setCommandMsgTemplateMsgCode(rs.getString("command_msg_template_msgcode"));
			item.setTemplateMsgCode(rs.getString("template_msgcode"));
			item.setField(rs.getString("field"));
			item.setRemark(rs.getString("remark"));
			return item;
		}

	}

	public static class BleEquipmentRowMapper implements org.springframework.jdbc.core.RowMapper<BleEquipment> {

		@Override
		public BleEquipment mapRow(ResultSet rs, int i) throws SQLException {
			BleEquipment item = new BleEquipment();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setEquipmentCode(rs.getString("equipment_code"));
			item.setUuid(rs.getString("uuid"));
			item.setMajor(rs.getString("major"));
			item.setMinor(rs.getString("minor"));
			item.setLocAddress(rs.getString("loc_address"));
			item.setCreateTime(rs.getTimestamp("create_time"));
			item.setIsActivity(rs.getInt("is_activity"));
			item.setIsEnabled(rs.getInt("is_enabled"));
			item.setRemark(rs.getString("remark"));
			return item;
		}

	}
	
	public static class PageIndexMapper implements org.springframework.jdbc.core.RowMapper<PageIndex> {

		@Override
		public PageIndex mapRow(ResultSet rs, int i) throws SQLException {
			PageIndex item = new PageIndex();

			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			
			item.setPageCode(rs.getString("page_code"));
			item.setLoginPage(rs.getString("login_page"));
			item.setTitle(rs.getString("title"));
			
			item.setManageIndexPage(rs.getString("manage_page"));
			
			return item;
		}

	}
}
