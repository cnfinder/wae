package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.wae.business.domain.SysConfig;


/***
 * 系统全局缓存配置 key-value pair 
 * 
 * @author Administrator
 *
 */
public class SysConfigCache{
	public static String CACHE_KEY="cache_sysconfig";
	
	private static Cache<String,String, SysConfig> cache;
	
	public void setCache(Cache<String,String, SysConfig> cache){
		SysConfigCache.cache=cache;
	}
	
	public Cache<String,String, SysConfig> getCache(){
		return SysConfigCache.cache;
	}
	
	
	/********************系统配置表 名称常量 ********************/
	
	public final static String KEY_SYSCONFIG_CONFIG_REGISTER_LINK="config_register_link";
	public final static String KEY_SYSCONFIG_CONFIG_REGISTER_RULE="config_register_rule";
	public final static String KEY_SYSCONFIG_CONFIG_WELCOME_USER_REGISTER="config_welcome_user_register";
	public final static String KEY_SYSCONFIG_CONFIG_USERNAME_DISABLE="config_username_disable";
	
	
	
	
	
	public final static String KEY_SYSCONFIG_CONFIG_NIPUSH_TIMER="config_nipush_timer";
	
	
	/**********************EMAIL CONFIG****************************/
	
	public final static String KEY_SYSCONFIG_CONFIG_MAIL_ACTIVITY_OPEN="config_mail_activity_open";
	public final static String KEY_SYSCONFIG_CONFIG_MAIL_USERNAME="config_mail_username";
	public final static String KEY_SYSCONFIG_CONFIG_MAIL_USERPWD="config_mail_userpwd";
	public final static String KEY_SYSCONFIG_CONFIG_MAIL_HOST="config_mail_host";
	public final static String KEY_SYSCONFIG_CONFIG_MAIL_PORT="config_mail_port";
	public final static String KEY_SYSCONFIG_CONFIG_MAIL_NICKNAME="config_mail_nickname";
	
	
	
	public final static String KEY_SYSCONFIG_CONFIG_USER_ACTIVE_EMAIL_TEMPATE="config_user_active_email_tempate";
	public final static String KEY_SYSCONFIG_CONFIG_USERPWD_RECOVERY_EMAIL_TEMPATE="config_userpwd_recovery_email_tempate";
	
	/**********************************************************/
	public final static String KEY_SYSCONFIG_CONFIG_SITE_BBNAME="config_site_bbname";
	
	
	public final static String KEY_SYSCONFIG_CONFIG_VERIFY_CODE_ACTIVITY="config_verify_code_activity";
	public final static String KEY_SYSCONFIG_CONFIG_LOGIN_URL="config_login_url";
	
	
	
	/*******是否解码********/
	public final static String KEY_SYSCONFIG_CONFIG_NEED_DECODE="config_need_decode";
	public final static String KEY_SYSCONFIG_CONFIG_FROM_ENCODE_TYPE="config_from_encode_type";
	public final static String KEY_SYSCONFIG_CONFIG_TO_ENCODE_TYPE="config_to_encode_type";
	
	//上传文件的路径
	public final static String KEY_SYSCONFIG_CONFIG_UPLOAD_FILE_ROOT_PATH="config_upload_file_root_path";
	//上传文件showtableconfigid
	public final static String KEY_SYSCONFIG_CONFIG_UPLOAD_FILE_SHOWTABLECONFIGID="config_upload_file_showtableconfigid";
	//文件表
	public final static String KEY_SYSCONFIG_CONFIG_UPLOAD_FILE_TABLE_NAME = "config_upload_file_table_name";
	
	//登陆是否需要验证码
	public final static String KEY_SYSCONFIG_CONFIG_SHOW_LOGIN_VALIDATE_IMG="config_show_login_validate_img";
	
	
	//导出EXCEL时的EXCEL模板文件
	public final static String KEY_SYSCONFIG_CONFIG_CONFIG_EXPORT_EXCEL_TEMPLATE_PATH="config_export_excel_template_path";
	
	
	//后台管理首页
	public final static String KEY_SYSCONFIG_CONFIG_CONFIG_MANAGE_MAIN_PAGE="config_manage_main_page";
	
	
	public final static String KEY_SYSCONFIG_CONFIG_CONFIG_MGR_MENU_LEVEL="config_mgr_menu_level";
	
	public final static String KEY_SYSCONFIG_CONFIG_CONTROL_TYPE_COMBOX_LOAD_DATA="config_control_type_combox_load_data";
	
	// 信号锁
	public static boolean KEY_LOCKED = false;
	
	
	
	//药物信息
	public static String KEY_CONFIG_DRUGINFO_SHOWTABLE_CONFIG_ID = "config_druginfo_showtable_config_id";
	
	//药物相互作用
	public static String KEY_CONFIG_DRUG_RECIPROCITY = "config_drug_reciprocity";
	//药品图片路径
	public static String KEY_CONFIG_DRUG_IMG_PATH = "config_drug_img_path";
	
	//药品说明书图片流
	public static String KEY_CONFIG_DRUG_SPEC_STREAM_SHOWTABLECONFIG = "config_drug_spec_stream_showtableconfig";
	
	
	//根据条码获取HIS编码
	public static String KEY_CONFIG_DRUG_HISCODE_BYBARCODE = "config_drug_hiscode_bybarcode";
	
	public static String KEY_CONFIG_DRUG_HISCODE_BYSUPERVISIONCODE = "config_drug_hiscode_bysupervisioncode";
	//备份存储路径
	public static final String BackUpPath = "config_back_up_file_path";  
	
	
	//是否启用接口日志
	public static String KEY_CONFIG_INTERFACELOG_ENABLE = "config_interfacelog_enable";
	
	
	//是否启用数据库操作日志
	public static String KEY_CONFIG_OPERATIONLOG_ENABLE = "config_operationlog_enable";
	
	
	//平台微信openid
	public static String KEY_CONFIG_SYS_GLOBAL_WX_OPENID = "config_sys_global_wx_openid";
	
	//400客服电话
	public static String KEY_CONFIG_SYS_400_PHONE = "config_sys_ip_400_phone";
	
	
	public SysConfigCache()
	{
		
	}
	public SysConfigCache(int size)
	{
	}
	
	public Map<String, SysConfig> getBaseDatas() {
		return get();
	}
	
	public SysConfig getValue(String key)
	{
		return cache.get(CACHE_KEY,key);
	}
	

	public boolean add(String key, SysConfig value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,SysConfig> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, SysConfig newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}



	
	public HashMap<String,SysConfig> get(){
		return cache.get(CACHE_KEY);
	}



	public SysConfig get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}



	public SysConfig getList(String key) {
		return cache.get(CACHE_KEY,key);
	}


	public List<SysConfig> getList(String key, long startIndex, long endIndex) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,startIndex, endIndex);
	}



	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
}
