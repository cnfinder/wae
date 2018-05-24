package cn.finder.wae.business.module.sys.dao;

import java.util.List;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.UserConfig;

/***
 * 系统模块  包括参数加载 全局量加载
 * @author wu hualong
 *
 */
public interface SysDao {
	List<UserConfig> loadUserConfigCache();

	List<SysConfig> loadSysConfigCache();
	
	
	List<ShowTableConfig> loadShowTableConfig();
	
	List<Constants>  loadConstantsCache();
	
	List<ServiceInterface> loadServiceInterfaceCache();
	public List<ShowDataConfig> findShowDataByShowTableName(String showTableName);
	
	
	List<PageIndex> loadPageIndexCache();
}
