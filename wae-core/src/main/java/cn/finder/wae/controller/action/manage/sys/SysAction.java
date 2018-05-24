package cn.finder.wae.controller.action.manage.sys;

import java.util.HashMap;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.UserConfig;
import cn.finder.wae.business.module.sys.service.SysService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.base.BaseActionSupport;

public class SysAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 115586558123300466L;

	private SysService sysService;

	private boolean ok = false;

	private String msg;

	public String refreshSysConfigCache() {
		try {

			HashMap<String,SysConfig> sysConfigs=sysService.loadSysConfigCache();
			ArchCache.getInstance().getSysConfigCache().clear();
			ArchCache.getInstance().getSysConfigCache().add(sysConfigs);
			
			ok = true;
		} catch (Exception e) {
			msg = "错误";
		}
		return SUCCESS;
	}

	public String refreshUserConfigCache() {
		try {

			HashMap<String, UserConfig> userConfigs=sysService.loadUserConfigCache();
			ArchCache.getInstance().getUserConfigCache().clear();
			ArchCache.getInstance().getUserConfigCache().add(userConfigs);
			ok = true;
		} catch (Exception e) {
			msg = "错误";
		}
		return SUCCESS;
	}
	
	public String refreshShowTableConfigCache() {
		try {
			HashMap<Long,ShowTableConfig> showTableConfigs=sysService.loadShowTable();
			ArchCache.getInstance().getShowTableConfigCache().clear();
			ArchCache.getInstance().getShowTableConfigCache().add(showTableConfigs);
			ok = true;
		} catch (Exception e) {
			msg = "错误";
		}
		return SUCCESS;
	}

	public String refreshRoleCache() {
		try {
			HashMap<Long, Role> roles=sysService.loadRoleCache();
			ArchCache.getInstance().getRoleCache().clear();
			ArchCache.getInstance().getRoleCache().add(roles);
			ok = true;
		} catch (Exception e) {
			msg = e.toString();
		}

		return SUCCESS;
	}
	
	public String refreshConstantsCache() {
		try {
			HashMap<Long,Constants> constants= sysService.loadConstantsCache();
			ArchCache.getInstance().getConstantsCache().clear();
			ArchCache.getInstance().getConstantsCache().add(constants);
			ok = true;
		} catch (Exception e) {
			msg = e.toString();
		}

		return SUCCESS;
	}
	
	public String refreshServiceInterfaceCache() {
		try {
			HashMap<String, ServiceInterface> serviceInterfaces=sysService.loadServiceInterfaceCache();
			ArchCache.getInstance().getServiceInterfaceCache().clear();
			ArchCache.getInstance().getServiceInterfaceCache().add(serviceInterfaces);
			ok = true;
		} catch (Exception e) {
			msg = e.toString();
		}

		return SUCCESS;
	}
	
	
	public String refreshAllCache(){
		try {
			HashMap<Long,Constants> constants= sysService.loadConstantsCache();
			ArchCache.getInstance().getConstantsCache().clear();
			ArchCache.getInstance().getConstantsCache().add(constants);
			
			
			HashMap<String,SysConfig> sysConfigs=sysService.loadSysConfigCache();
			ArchCache.getInstance().getSysConfigCache().clear();
			ArchCache.getInstance().getSysConfigCache().add(sysConfigs);
			

			
			HashMap<Long,ShowTableConfig> showTableConfigs=sysService.loadShowTable();
			ArchCache.getInstance().getShowTableConfigCache().clear();
			ArchCache.getInstance().getShowTableConfigCache().add(showTableConfigs);
			
			
			
			
			
			HashMap<Long, Role> roles=sysService.loadRoleCache();
			ArchCache.getInstance().getRoleCache().clear();
			ArchCache.getInstance().getRoleCache().add(roles);
			
			
		//****** loading other cache *************//
			//load Menu cache
			
			
			
			HashMap<String, ServiceInterface> serviceInterfaces=sysService.loadServiceInterfaceCache();
			ArchCache.getInstance().getServiceInterfaceCache().clear();
			ArchCache.getInstance().getServiceInterfaceCache().add(serviceInterfaces);

			
			HashMap<String, UserConfig> userConfigs=sysService.loadUserConfigCache();
			ArchCache.getInstance().getUserConfigCache().clear();
			ArchCache.getInstance().getUserConfigCache().add(userConfigs);
			ok = true;
		} catch (Exception e) {
			msg = e.toString();
		}

		
		
		return SUCCESS;
	}
	

	public String cacheMgr() {
		return SUCCESS;
	}

	public void setSysService(SysService sysService) {
		this.sysService = sysService;
	}

	public boolean isOk() {
		return ok;
	}

	public String getMsg() {
		return msg;
	}

}
