package cn.finder.wae.business.module.common.dao;

import java.util.List;
import java.util.Map;

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

public interface CommonDao {

	List<SysConfig> loadCache();

	UserSetting findUserSettingByUserName(String userName);

	ShowtableConfigProcess findShowtableConfigProcess(long showtableConfigId);

	/***
	 * 查找用户配置 值
	 * 
	 * @param key
	 * @return
	 */
	String findUserConfig(String key);

	/***
	 * 更新用户配置表 数据
	 * 
	 * @param key
	 * @return
	 */
	int updateUserConfig(String key, String value);

	/***
	 * 添加服务调用日志
	 * 
	 * @param serviceInterfaceLog
	 * @return
	 */
	int addServiceInterfaceLog(ServiceInterfaceLog serviceInterfaceLog);

	/***
	 * 服务接口数据更新 (更新 完成时间 和 返回 数据)
	 * 
	 * @param serviceInterfaceLog
	 * @return
	 */
	int updateServiceInterfaceLog(ServiceInterfaceLog serviceInterfaceLog);

	/***
	 * 更新SDK文件路径
	 * 
	 * @param id
	 * @param sdkFile
	 * @return
	 */
	int updateServiceInterfaceSDK(long id, String sdkFile);

	public int[] addRoleRequest(final long roleId, final String[] requestIds);

	/***
	 * 获取微信APP信息
	 * 
	 * @param id
	 * @return
	 */
	public AppInfo findAppInfo(int id);

	/***
	 * 获取所有APP相关信息
	 * 
	 * @return
	 */
	public List<AppInfo> findAppInfo();

	/***
	 * 获取可以接收的 微信公众号管理员呢
	 * 
	 * @return
	 */
	public List<WXAppInfoManager> findWXAppInfoManagerForCanReceive(int appinfo_id);

	/**
	 * 通过用户账号获取用户id
	 * 
	 * @param account
	 * @return
	 */
	public User findUser(String account);

	/***
	 * 根据模板编码 获取模板消息
	 * 
	 * @param templateCode
	 * @return
	 */
	public TemplateMsg findTemplateMsg(String templateCode);

	/***
	 * 加载微信命令到缓存
	 */
	public List<WXCommand> loadWXCommand();

	/**
	 * 用户注册
	 * 
	 * @param user
	 */
	public int addUser(User user);

	/***
	 * 获取企业号管理权限信息
	 * 
	 * @return
	 */
	public List<CorpAdminGroup> findCorpAdminGroup();

	/***
	 * 获取企业号信息
	 * 
	 * @param corpInfoId
	 * @return
	 */
	public CorpInfo findCorpInfo(int corpInfoId);

	/**
	 * 获取属性模板
	 * 
	 * @param propertiesTemplateTypeCode
	 * @return
	 */
	public List<PropertiesTemplateItem> findPropertiesTemplate(String propertiesTemplateTypeCode);

	/**
	 * oa流程添加
	 * 
	 * @param oAFlowBase
	 */
	public OAFlowBase addOAFlowBase(OAFlowBase oAFlowBase);

	/**
	 * oa流程附件添加
	 * 
	 * @param
	 */
	public int addOAFlowBaseAttach(String business_key, String media_id);

	/**
	 * oa流程任务记录添加
	 * 
	 * @param
	 */
	public int addOAFlowTaskRecord(OAFlowTaskRecord taskRecord);

	/**
	 * 根据busuness_key查找OA流程
	 * 
	 * @param
	 */
	public OAFlowBase findOAFlowBase(String business_key);

	/***
	 * 获取OA工作流程列表
	 * 
	 * @return
	 */
	public List<OAFlowItem> findFlows();

	/***
	 * 获取最大的设备ID
	 * 
	 * @return
	 */
	public String bleGeneratorNewEquipmentCode();

	/***
	 * 获取最大的Major
	 * 
	 * @return
	 */
	public String bleGeneratorNewMajor();

	/***
	 * 获取最大的Minor
	 * 
	 * @return
	 */
	public String bleGeneratorNewMinor();

	/***
	 * 添加BLE设备
	 * 
	 * @param items
	 * @return
	 */
	public int addBleEquipment(BleEquipment item);

	/***
	 * 获取用户图表列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Chart> findChartByUserId(String userId);

	public boolean isBleEquipmentExists(int currentId, String uuid, String major, String minor);

	/**
	 * 添加菜单到APP
	 * 
	 * @param menu_id
	 * @param produce_id
	 * @param user_id
	 */
	public void addMenuToApp(int menu_id, int product_id, String user_id);

	/**
	 * 删除APP菜单
	 * 
	 * @param produce_id
	 * @param user_id
	 */
	public void deleteMenuFromApp(int product_id, String user_id);

	/**
	 * 添加图表到用户
	 * 
	 * @param char_id
	 * @param user_id
	 */
	public void addChartToUser(int char_id, String user_id);

	/**
	 * 删除图表到用户
	 * 
	 * @param user_id
	 */
	public void deleteChartFromUser(String user_id);

	/***
	 * @param commandName
	 * @return
	 */
	public ScannerCode findScannerCode(String commandName);

	/**
	 * oa流程获取任务数量
	 * 
	 * @param
	 */
	public int findFlowTaskRecord(OAFlowTaskRecord taskRecord);

	/**
	 * 根据用户名获取任务数量
	 * 
	 * @param taskRecord
	 * @return
	 */
	public int findFlowTaskRecordByUserId(OAFlowTaskRecord taskRecord);

	/**
	 * 根据code查找区域ble设备
	 * 
	 * @param code
	 * @return
	 */
	public List<BleEquipment> findBleEquipmentByCode(String code);

	/**
	 * 查询派工单进程
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findWorkorderProgress();
	
	/***
	 * 获取微信菜单关联的 media_id，根据 eventKey
	 * @param wx_appinfo_id
	 * @param eventKey
	 * @return
	 */
	public String findWxMenuMediaIdByEventKey(int wx_appinfo_id,String eventKey);
	
	
	/***
	 * 根据危险appid获取
	 * @param appid
	 * @return
	 */
	public AppInfo findAppInfoByWXAppId(String appid);
	
	/***
	 * 获取微信菜单 指定的子菜单
	 * @param wx_appinfo_id
	 * @param parentId
	 * @return
	 */
	public List<Map<String,Object>> findWxMenus(int wx_appinfo_id,int parentId);
	
	
	/***
	 * 获取某商家 SMS配置
	 * @param mch_code
	 * @return
	 */
	public Map<String,Object> findSmsConfig(String mch_code);
	
	
	/***
	 * 发送消息后 保存内容
	 * @param mch_code
	 * @return
	 */
	public String saveSmsMsg(Map<String,Object> msg);
	

	/***
	 * 验证码有效性验证
	 * @param valid_code
	 * @return
	 */
	public String validSmsValidCode(String id,String valid_code); 
	
	
	/***
	 * 获取一个号码运营了多少商家
	 * @param phone
	 * @return
	 */
	public int findPhoneNumberHasMchNumber(String phone);
	
	/**
	 * 根据角色id获取用户
	 * @param roleId
	 * @return
	 */
	public List<User> findUserByRoleId(int roleId);
	
	/****
	 * 微信菜单和 资源关联
	 * @param menu_id
	 * @param media_id
	 * @return
	 */
	public int updateWXMenuMediaLink(int menu_id,String media_id);
	
	/***
	 * 获取 微信 app
	 * @param appid
	 * @return
	 */
	public List<Map<String,Object>> findWxApp(String appid);
	
	
	/***
	 * 更新服务号的token状态
	 * @param appid
	 * @param status
	 * @return
	 */
	public int updateWxAppInfoTokenStatus(String appid,String status);

	/***
	 * 更新企业号的token状态
	 * @param appid
	 * @param status
	 * @return
	 */
	public int updateWxCorpTokenStatus(String appid,String status);
	
}
