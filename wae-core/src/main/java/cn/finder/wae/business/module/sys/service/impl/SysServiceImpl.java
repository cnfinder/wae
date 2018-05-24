package cn.finder.wae.business.module.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.MetaData;
import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.RequestCommand;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.RoleMenu;
import cn.finder.wae.business.domain.RoleReqCommand;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.TableField;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.domain.UserConfig;
import cn.finder.wae.business.module.dataImport.dao.DataImportDao;
import cn.finder.wae.business.module.sys.dao.MenuDao;
import cn.finder.wae.business.module.sys.dao.RoleDao;
import cn.finder.wae.business.module.sys.dao.SysDao;
import cn.finder.wae.business.module.sys.dao.TableFieldDao;
import cn.finder.wae.business.module.sys.service.SysService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.MetaDataCache;
import cn.finder.wae.cache.PageIndexCache;
import cn.finder.wae.cache.RoleCache;
import cn.finder.wae.cache.ServiceInterfaceCache;
import cn.finder.wae.cache.ShowTableConfigCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.cache.UserConfigCache;
import cn.finder.wae.common.thread.MainExecutor;

public class SysServiceImpl implements SysService{

	
	private static Logger logger  = Logger.getLogger(SysServiceImpl.class);
	
	private SysDao sysDao;
	private MenuDao menuDao;
	private RoleDao roleDao;
	private TableFieldDao tableFieldDao;
	
	private DataImportDao dataImportDao;
	private MainExecutor me;
	
	public void setMe(MainExecutor me){
		this.me = me;
	}
	public void setSysDao(SysDao sysDao)
	{
		this.sysDao = sysDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	
	public void setTableFieldDao(TableFieldDao tableFieldDao) {
		this.tableFieldDao = tableFieldDao;
	}
	
	public void setDataImportDao(DataImportDao dataImportDao){
		this.dataImportDao = dataImportDao;
	}
	@Override
	public HashMap<String,SysConfig> loadSysConfigCache() {
		
		HashMap<String,SysConfig> data=new HashMap<String, SysConfig>();
		
		List<SysConfig> list = sysDao.loadSysConfigCache();
		
		//SysConfigCache sysConfigCache = ArchCache.getInstance().getSysConfigCache();
		
		//加载就是完全重新加载
		//sysConfigCache.clear();
		
		for(SysConfig nv:list)
		{
			data.put(nv.getName(),nv);
		}
		
		return data;
	}

	@Override
	public HashMap<Long, ShowTableConfig> loadShowTable() {

		HashMap<Long, ShowTableConfig> data=new HashMap<Long, ShowTableConfig>();
		
		List<ShowTableConfig> tableConfigs = sysDao.loadShowTableConfig();
		
		//ShowTableConfigCache showTableConfigCache = ArchCache.getInstance().getShowTableConfigCache();
		
		//showTableConfigCache.clear();
		
		
		
		for(ShowTableConfig nv:tableConfigs)
		{
			data.put(nv.getId(),nv);
		}
		return data;
		
	}

	@Override
	//加载角色-权限缓存
	public HashMap<Long, Role> loadRoleCache() {
		
		HashMap<Long, Role> data=new HashMap<Long, Role>();
		
		List<Role> roles = roleDao.findAll();
		if(roles != null && roles.size() > 0){
			this.generateTree(roles);
			
			List<RoleMenu> roleMenus = menuDao.findAllRoleMenus();
			List<RoleReqCommand> rolerequestCommandsAll = roleDao.findAllRoleReqCommd();
			
			for(Role role:roles){
				
				//处理 RequestCommand权限
				for(RoleReqCommand rrc:rolerequestCommandsAll){
					if(rrc.getRole_id().intValue()==role.getId().intValue()){
						RequestCommand rc=new RequestCommand();
						rc.setId(rrc.getRequest_command_id());
						rc.setName(rrc.getRequestCommandName());
						rc.setCommand(rrc.getCommand());
						role.getReqs().add(rc);
					}
				}
				//==============处理menus===================//
				List<Menu> menus=new ArrayList<Menu>();
				for(RoleMenu rm:roleMenus){
					if(rm.getRoleId()==role.getId()){
						menus.add(rm.getMenu());
					}
				}
				
				
				///生成树形的menu
				menus = menuDao.generateTree(menus);
				role.setMenus(menus);
				
				
				data.put(role.getId(), role);
			}
		}
		return data;
		
	}
	
	/***
	 * 
	 * @param roles
	 * @return
	 */
	private List<Role> generateTree(List<Role> roles) {
		for(Role role:roles){
			List<Role> children = new ArrayList<Role>();
			for(Role child:roles){
				if(child.getParentId() != null && child.getParentId().longValue() == role.getId()){
					children.add(child);
				}
				if(role.getParentId() != null  && role.getParentId().longValue() == child.getId()){
					role.setParent(child);
				}
			}
			role.setChildren(children);
		}
		return roles;
	}
	
	/***
	 * 构建角色树，根据角色使用递归获取其对应的子权限
	 * @param role
	 * @return
	 */
	public Role generateRole(Role role){
		/*List<RoleMenu> roleMenus = roleMenuDao.findMenusByRoleId(role.getId());
		List<RequestCommand> requestCommands = roleReqCommdDao.findReqCommdsByRoleId(role.getId()); 
		
		if(roleMenus != null && roleMenus.size() > 0){
			List<Menu> menus = new ArrayList<Menu>();
			for(RoleMenu roleMenu : roleMenus){
				
				Menu m = menuDao.findMenuById(roleMenu.getMenuId());
				if(m==null){
					logger.debug("=====no refrence roleid:"+ roleMenu.getRoleId()+" menuid: "+roleMenu.getMenuId());
				}
						
				
				menus.add(m);
			}
			for(Menu menu: menus){
				menus = menuDao.generateTree(menus);
			}
			role.setMenus(menus);
		}
		
		role.setReqs(requestCommands);
		return role;*/
		
		throw new RuntimeException("the method is unimplement");
		
		
	}

	@Override
	public HashMap<Long,Constants> loadConstantsCache() {
		// TODO Auto-generated method stub
		HashMap<Long,Constants> data=new HashMap<Long,Constants>();
		List<Constants> list = sysDao.loadConstantsCache();
		
		//ConstantsCache constantsCache = ArchCache.getInstance().getConstantsCache();
		
		//加载就是完全重新加载
		//constantsCache.clear();
		
		for(Constants nv:list)
		{
			data.put(nv.getId(),nv);
		}
		return data;
		
	}

	@Override
	public HashMap<String,MetaData> loadMetaCache() {
		
		HashMap<String,MetaData> data=new HashMap<String, MetaData>(); 
				
		
		//MetaDataCache metaCache = ArchCache.getInstance().getMetaDataCache();
		/*List<TableField> fields = tableFieldDao.findAll();
		if(fields != null && fields.size()>0){
			for(TableField field:fields){
				String tableName = field.getTableName();
				if(metaCache.get(tableName) ==null){
					MetaData metaData = new MetaData();
					List<TableField> list = new ArrayList<TableField>();
					list.add(field);
					metaData.setFields(list);
					metaCache.add(tableName, metaData);
				}else{
					MetaData metaData = metaCache.get(tableName);
					metaData.getFields().add(field);
				}
			}
		}*/
		return null;
		
		
		
	}

	@Override
	public void loadDataImportCache() {
//		 DataImportCache dic = ArchCache.getInstance().getDataImportCache();
		
		if(dataImportDao!=null){
		
			 List<DataImportConfig> dics = dataImportDao.findThreads();
			 
			 for(DataImportConfig dataImportConfig: dics){
				 if(dataImportConfig.getStartWithServer() > 0){
					 dataImportDao.startNewThread(dataImportConfig);
				 }
				
				// dataImportDao.setRunningStateYes(dataImportConfig.getId());
			 }
		}
//		me.run();
	}
	/* (non-Javadoc)
	 * @see cn.finder.wae.business.module.sys.service.SysService#loadServiceInterfaceCache()
	 */
	@Override
	public HashMap<String, ServiceInterface> loadServiceInterfaceCache() {

		HashMap<String, ServiceInterface> data=new HashMap<String, ServiceInterface>();
		
		List<ServiceInterface> list= sysDao.loadServiceInterfaceCache();
		
		
		
	//	ServiceInterfaceCache serviceInterfaceCache = ArchCache.getInstance().getServiceInterfaceCache();
		
		//加载就是完全重新加载
		//serviceInterfaceCache.clear();
		
		for(ServiceInterface nv:list)
		{
			data.put(nv.getInterfaceName(),nv);
		}
		return data;
	}
	@Override
	public HashMap<String, UserConfig> loadUserConfigCache() {
		// TODO Auto-generated method stub
		HashMap<String, UserConfig> data=new HashMap<String, UserConfig>();
		List<UserConfig> list = sysDao.loadUserConfigCache();
		
		//UserConfigCache userConfigCache = ArchCache.getInstance().getUserConfigCache();
		
		//加载就是完全重新加载
		//userConfigCache.clear();
		
		for(UserConfig nv:list)
		{
			data.put(nv.getName(),nv);
		}
		return data;
	}
	@Override
	public Role findById(Long roleId) {
		
		return roleDao.findById(roleId);
	}
	@Override
	public Role findByRoleCode(String roleCode) {
		
		return roleDao.findByRoleCode(roleCode);
	}
	
	
	@Override
	public List<Menu> findPrivilegeMenusByUserId(String userId) {
		// TODO Auto-generated method stub
		
		List<Menu> privilegeMenus=menuDao.findPrivilegeMenusByUserId(userId);
		
		///生成树形的menu
		List<Menu> privilegeTree = menuDao.generateTree(privilegeMenus);
		
		return privilegeTree;
	}
	@Override
	public List<Menu> findUserMgrMenus(User user) {
		
		List<Menu> roleMenus=menuDao.findMenusByRoleId(user.getRoleId());
		
		List<Menu> privilegeMenus=menuDao.findPrivilegeMenusByUserId(user.getAccount());
		
		if(privilegeMenus!=null&&privilegeMenus.size()>0){
			
			
			for(Menu m:privilegeMenus){
				boolean isExists=false;
				for(Menu rm:roleMenus){
					if(rm.getId()==m.getId()){
						isExists=true;
						break;
					}
				}
				if(!isExists){
					//加入插入顺序
					//roleMenus.add(m);
					
					
					boolean inserted=false;
					for(int i=0;i<roleMenus.size();i++){
						Menu rm= roleMenus.get(i);
						if(rm.getSort()>=m.getSort()){
							if((i+1)<roleMenus.size()){
								Menu rm_next=roleMenus.get(i+1);
								if(m.getSort()<rm_next.getSort()){
									roleMenus.add(i+1, m);
									inserted=true;
									break;
								}else{
									continue;
								}
							}else{
								roleMenus.add(i+1, m);
								inserted=true;
								break;
							}
							
							
							
						}
					}
					if(!inserted)
						roleMenus.add(m);
				}
				
			}
			
		}
		
		List<Menu> menus = menuDao.generateTree(roleMenus);
		
		
		return menus;
	}
	@Override
	public HashMap<String, PageIndex> loadPageIndexCache() {
		HashMap<String, PageIndex> data=new HashMap<String, PageIndex>();
		
		List<PageIndex> list = sysDao.loadPageIndexCache();
		
		//PageIndexCache pageIndexCache = ArchCache.getInstance().getPageIndexCache();
		
		//加载就是完全重新加载
		//pageIndexCache.clear();
		
		for(PageIndex nv:list)
		{
			data.put(nv.getPageCode(),nv);
		}
		return data;
	}
	
	
	

}
