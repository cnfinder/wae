package cn.finder.wae.business.module.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.justobjects.pushlet.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.Menu;
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
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.business.module.sys.dao.MenuDao;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.cache.WXCommandCache;
import cn.finder.wae.common.comm.templatemsg.AppTemplateMsgSender;
import cn.finder.wae.common.comm.templatemsg.WXCorpTemplateMsgSender;
import cn.finder.wae.common.comm.templatemsg.WXTemplateMsgSender;
import cn.finder.wae.common.comm.templatemsg.WebTemplateMsgSender;
import cn.finder.wae.pushlet.PushWebSender;

public class CommonServiceImpl implements CommonService {

	private Logger logger = Logger.getLogger(CommonServiceImpl.class);
	private CommonDao commonDao;
	private MenuDao menuDao;

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public void loadCache() {

		List<SysConfig> list = commonDao.loadCache();

		SysConfigCache sysConfigCache = ArchCache.getInstance().getSysConfigCache();

		// 加载就是完全重新加载
		sysConfigCache.clear();

		for (SysConfig nv : list) {
			sysConfigCache.add(nv.getName(), nv);
		}

	}

	@Override
	public UserSetting findUserSettingByUserName(String userName) {
		// TODO Auto-generated method stub
		UserSetting userSetting = commonDao.findUserSettingByUserName(userName);
		if (userSetting == null) {
			userSetting = new UserSetting();
		}

		if (!StringUtils.isEmpty(userSetting.getFirstMenuPage())) {
			Menu menu = menuDao.findMenuById(Long.parseLong(userSetting.getFirstMenuPage()));

			userSetting.setFirstMenu(menu);
		}

		return userSetting;
	}

	/*
	 * (non-Javadoc)
	 * @see cn.finder.wae.business.module.common.service.CommonService#
	 * findShowtableConfigProcess(long)
	 */
	@Override
	public ShowtableConfigProcess findShowtableConfigProcess(long showtableConfigId) {
		// TODO Auto-generated method stub
		return commonDao.findShowtableConfigProcess(showtableConfigId);
	}

	@Override
	public int updateUserConfig(String key, String value) {
		// TODO Auto-generated method stub
		return commonDao.updateUserConfig(key, value);
	}

	@Override
	public String findUserConfig(String key) {
		// TODO Auto-generated method stub
		return commonDao.findUserConfig(key);
	}

	@Override
	public int addServiceInterfaceLog(ServiceInterfaceLog serviceInterfaceLog) {
		// TODO Auto-generated method stub
		return commonDao.addServiceInterfaceLog(serviceInterfaceLog);
	}

	@Override
	public int updateServiceInterfaceLog(ServiceInterfaceLog serviceInterfaceLog) {
		// TODO Auto-generated method stub
		return commonDao.updateServiceInterfaceLog(serviceInterfaceLog);
	}

	@Override
	public int updateServiceInterfaceSDK(long id, String sdkFile) {
		// TODO Auto-generated method stub
		return commonDao.updateServiceInterfaceSDK(id, sdkFile);
	}

	@Override
	public int[] addRoleRequest(long roleId, String[] requestIds) {
		return commonDao.addRoleRequest(roleId, requestIds);

	}

	@Override
	public AppInfo findAppInfo(int id) {
		// TODO Auto-generated method stub
		return commonDao.findAppInfo(id);
	}

	@Override
	public List<AppInfo> findAppInfo() {
		// TODO Auto-generated method stub
		return commonDao.findAppInfo();
	}

	@Override
	public List<WXAppInfoManager> findWXAppInfoManagerForCanReceive(int appinfo_id) {
		return commonDao.findWXAppInfoManagerForCanReceive(appinfo_id);
	}

	@Override
	public User findUser(String account) {

		return commonDao.findUser(account);
	}

	@Override
	public TemplateMsg findTemplateMsg(String templateCode) {
		// TODO Auto-generated method stub
		return commonDao.findTemplateMsg(templateCode);
	}

	@Override
	public HashMap<String, WXCommand> loadWXCommand() {
		
		HashMap<String, WXCommand> data=new HashMap<String, WXCommand>();
		
		List<WXCommand> wxcommandList = commonDao.loadWXCommand();
		if (wxcommandList != null && wxcommandList.size() > 0) {
			//WXCommandCache wxcmdcache = ArchCache.getInstance().getWxCommandCache();
			// 加载就是完全重新加载
			//wxcmdcache.clear();

			for (WXCommand nv : wxcommandList) {
				data.put(nv.getCommandStr(), nv);
			}
		}
		return data;
	}

	@Override
	public int addUser(User user) {
		return commonDao.addUser(user);
	}

	@Override
	public List<CorpAdminGroup> findCorpAdminGroup() {
		// TODO Auto-generated method stub
		return commonDao.findCorpAdminGroup();
	}

	@Override
	public CorpInfo findCorpInfo(int corpInfoId) {
		// TODO Auto-generated method stub
		return commonDao.findCorpInfo(corpInfoId);
	}

	@Override
	public void sendAppTemplateMsg(Map<String, Object> variables) {
		// 应该是获取到当前 服务任务中设置的 变量

		Object templateMsgCode = variables.get("templateMsgCode");
		if (templateMsgCode == null)
			logger.error("can't find parameter:templateMsgCode");

		TemplateMsg tm = findTemplateMsg(templateMsgCode.toString());

		AppTemplateMsgSender sender = new AppTemplateMsgSender(tm);

		sender.send(variables);

	}

	@Override
	public void sendWebPushTemplateMsg(Map<String, Object> variables) {
		/*
		Object templateMsgCode = variables.get("templateMsgCode");
		if (templateMsgCode == null)
			logger.error("can't find parameter:templateMsgCode");

		TemplateMsg tm = findTemplateMsg(templateMsgCode.toString());

		WebTemplateMsgSender sender = new WebTemplateMsgSender(tm);

		sender.send(variables);
		*/
		try{
			PushWebSender sender=new PushWebSender();
			String subject = variables.get("subject").toString();
			String user_id = variables.get("toUserId").toString();
			String[] users = user_id.split(",");
			for(String user:users){
				sender.sendToUser(subject, user, variables);
			}
		}catch (Exception e) {
			
		}
	}

	@Override
	public void sendWXCorpNoticeTextMsg(Map<String, Object> variables) {

		Object templateMsgCode = variables.get("wx_corp_templateMsgCode");

		Object wx_appid = variables.get("wx_appid");
		if (wx_appid == null || wx_appid == "" || wx_appid.toString().length() == 0) {
			logger.info("==========wx_appid为空==========");
			return;
		}

		TemplateMsg tm = findTemplateMsg(templateMsgCode.toString());

		WXCorpTemplateMsgSender wxTemplateMsgSender = new WXCorpTemplateMsgSender(tm);
		String[] users = variables.get("toUserId").toString().split(",");
		logger.info("===============微信群发开始=====================");
		for (String user : users) {
			variables.put("toUserId", user);
			variables.put("task_user_id", user);
			Log.info("用户：" + variables.get("toUserId").toString());
			wxTemplateMsgSender.send(variables);
		}
		logger.info("===============微信群发结束=====================");

	}

	@Override
	public void sendWXNoticeMsg(Map<String, Object> variables) {
		// 应该是获取到当前 服务任务中设置的 变量

		Object templateMsgCode = variables.get("templateMsgCode");

		TemplateMsg tm = findTemplateMsg(templateMsgCode.toString());

		WXTemplateMsgSender wxTemplateMsgSender = new WXTemplateMsgSender(tm);

		wxTemplateMsgSender.send(variables);

	}

	@Override
	public List<PropertiesTemplateItem> findPropertiesTemplate(String propertiesTemplateTypeCode) {
		List<PropertiesTemplateItem> propertiesTemplateItem = commonDao.findPropertiesTemplate(propertiesTemplateTypeCode);

		return propertiesTemplateItem;

	}

	@Override
	public OAFlowBase addOAFlowBase(OAFlowBase oAFlowBase) {
		return commonDao.addOAFlowBase(oAFlowBase);
	}

	@Override
	public int addOAFlowBaseAttach(String business_key, String media_id) {
		return commonDao.addOAFlowBaseAttach(business_key, media_id);
	}

	@Override
	public int addOAFlowTaskRecord(OAFlowTaskRecord taskRecord) {
		return commonDao.addOAFlowTaskRecord(taskRecord);
	}

	@Override
	public OAFlowBase findOAFlowBase(String business_key) {
		return commonDao.findOAFlowBase(business_key);
	}

	@Override
	public List<OAFlowItem> findFlows() {
		// TODO Auto-generated method stub
		return commonDao.findFlows();
	}

	@Override
	public void addMenuToApp(int menu_id, int product_id, String user_id) {
		commonDao.addMenuToApp(menu_id, product_id, user_id);
	}

	@Override
	public String bleGeneratorNewEquipmentCode() {
		// TODO Auto-generated method stub
		return commonDao.bleGeneratorNewEquipmentCode();
	}

	@Override
	public String bleGeneratorNewMajor() {
		// TODO Auto-generated method stub
		return commonDao.bleGeneratorNewMajor();
	}

	@Override
	public String bleGeneratorNewMinor() {
		// TODO Auto-generated method stub
		return commonDao.bleGeneratorNewMinor();
	}

	@Override
	public int addBleEquipment(BleEquipment item) {
		// TODO Auto-generated method stub
		return commonDao.addBleEquipment(item);
	}

	@Override
	public boolean isBleEquipmentExists(int currentId, String uuid, String major, String minor) {
		// TODO Auto-generated method stub
		return commonDao.isBleEquipmentExists(currentId, uuid, major, minor);
	}

	@Override
	public void deleteMenuFromApp(int product_id, String user_id) {
		commonDao.deleteMenuFromApp(product_id, user_id);
	}

	@Override
	public void addChartToUser(int char_id, String user_id) {
		commonDao.addChartToUser(char_id, user_id);
	}

	@Override
	public void deleteChartFromUser(String user_id) {
		commonDao.deleteChartFromUser(user_id);
	}

	@Override
	public List<Chart> findChartByUserId(String userId) {
		// TODO Auto-generated method stub
		return commonDao.findChartByUserId(userId);
	}

	@Override
	public ScannerCode findScannerCode(String commandName) {
		logger.info("===public ScannerCode findScannerCode(String commandName)");
		return commonDao.findScannerCode(commandName);
	}

	@Override
	public int findFlowTaskRecord(OAFlowTaskRecord taskRecord) {
		return commonDao.findFlowTaskRecord(taskRecord);
	}

	@Override
	public int findFlowTaskRecordByUserId(OAFlowTaskRecord taskRecord) {
		return commonDao.findFlowTaskRecordByUserId(taskRecord);
	}

	@Override
	public List<BleEquipment> findBleEquipmentByCode(String code) {
		return commonDao.findBleEquipmentByCode(code);
	}

	@Override
	public List<Map<String, Object>> findWorkorderProgress() {
		return commonDao.findWorkorderProgress();
	}

	@Override
	public AppInfo findAppInfoByWXAppId(String appid) {
		// TODO Auto-generated method stub
		return commonDao.findAppInfoByWXAppId(appid);
	}

	@Override
	public List<Map<String, Object>> findWxMenus(int wx_appinfo_id, int parentId) {
		// TODO Auto-generated method stub
		return commonDao.findWxMenus(wx_appinfo_id,parentId);
	}

	@Override
	public String findWxMenuMediaIdByEventKey(int wx_appinfo_id, String eventKey) {
		// TODO Auto-generated method stub
		return commonDao.findWxMenuMediaIdByEventKey(wx_appinfo_id, eventKey);
	}

	@Override
	public Map<String, Object> findSmsConfig(String mch_code) {
		
		return commonDao.findSmsConfig(mch_code);
	}

	@Override
	public String saveSmsMsg(Map<String, Object> msg) {
		// TODO Auto-generated method stub
		return commonDao.saveSmsMsg(msg);
	}

	@Override
	public String validSmsValidCode(String id,String valid_code) {
		// TODO Auto-generated method stub
		return commonDao.validSmsValidCode(id, valid_code);
	}

	@Override
	public int findPhoneNumberHasMchNumber(String phone) {
		// TODO Auto-generated method stub
		return commonDao.findPhoneNumberHasMchNumber(phone);
	}

	@Override
	public List<User> findUserByRoleId(int roleId) {
		// TODO Auto-generated method stub
		return commonDao.findUserByRoleId(roleId);
	}

	@Override
	public int updateWXMenuMediaLink(int menu_id, String media_id) {
		// TODO Auto-generated method stub
		return commonDao.updateWXMenuMediaLink(menu_id, media_id);
	}

	@Override
	public List<Map<String, Object>> findWxApp(String appid) {
		// TODO Auto-generated method stub
		return commonDao.findWxApp(appid);
	}

	@Override
	public int updateWxAppInfoTokenStatus(String appid, String status) {
		return commonDao.updateWxAppInfoTokenStatus(appid, status);
	}

	@Override
	public int updateWxCorpTokenStatus(String appid, String status) {
		
		return commonDao.updateWxCorpTokenStatus(appid, status);
	}

}
