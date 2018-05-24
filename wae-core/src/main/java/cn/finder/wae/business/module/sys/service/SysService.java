package cn.finder.wae.business.module.sys.service;

import java.util.HashMap;
import java.util.List;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.MetaData;
import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.domain.UserConfig;


public interface SysService {

	HashMap<String,SysConfig> loadSysConfigCache();
	
	HashMap<Long,Constants> loadConstantsCache();
	
	HashMap<Long,ShowTableConfig> loadShowTable();
	
	HashMap<Long, Role> loadRoleCache();
	
	@Deprecated
	HashMap<String,MetaData> loadMetaCache();
	
	void loadDataImportCache();
	
	HashMap<String, ServiceInterface> loadServiceInterfaceCache();
	
	HashMap<String, UserConfig> loadUserConfigCache();
	
	
	HashMap<String, PageIndex> loadPageIndexCache();
	
	public Role findById(Long roleId);
	
	
	
	public Role findByRoleCode(String roleCode);
	
	/***
	 * 根据用户获取私有菜单(返回树形结构)
	 * @param userId
	 * @return
	 */
	public List<Menu> findPrivilegeMenusByUserId(String userId);
	
	
	/***
	 * 获取用户的所有授权菜单
	 * @param user
	 * @return
	 */
	public List<Menu> findUserMgrMenus(User user);
}
