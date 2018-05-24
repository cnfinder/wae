package cn.finder.wae.business.module.sys.dao.impl;

import java.util.ArrayList;
import java.util.List;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.GridMenu;
import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.UserConfig;
import cn.finder.wae.business.module.sys.dao.SysDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class SysDaoImpl extends BaseJdbcDaoSupport implements SysDao{

	@Override
	public List<SysConfig> loadSysConfigCache() {
		List<SysConfig> list = getJdbcTemplate().query("select * from v_sys_config",new RowMapperFactory.SysConfigRowMapper());
		return list;
	}

	@Override
	public List<ShowTableConfig> loadShowTableConfig() {
		// TODO Auto-generated method stub
		/*String sql ="select * from v_showtable_config ORDER BY ID ASC";
		
		 List<ShowTableConfig> tableConfigs= getJdbcTemplate().query(sql,new RowMapperFactory.ShowTableConfigRowMapper());
		 if(tableConfigs !=null)
		 {
			 for(ShowTableConfig tc:tableConfigs)
			 {
				 
				 //加载 show data
				 tc.setShowDataConfigs(findShowDataByShowTableName(tc.getShowTableName()));
				 
				 //加载 grid menu
				 
				 List<GridMenu> menus = findGridMenuByShowTableConfigId(tc.getId());
				 if(menus!=null)
				 {
					 for(GridMenu gm:menus)
					 {
						 if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_TOOLBAR)
						 {
							 tc.getToolBarMenus().add(gm);
							 
						 }else  if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_GRID_FRONT)
						 {
							 tc.getRowFrontMenus().add(gm);
							 
						 }
						 else  if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_GRID_BACK)
						 {
							 tc.getRowBackMenus().add(gm);
							 
						 }
						 else  if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_GRID_CONTEXT_MENU)
						 {
							 tc.getContextMenus().add(gm);
							 
						 }
					 }
				 }
				 
			 }
		 }
		return tableConfigs;
		*/
		
		
		String sql ="select * from v_showtable_config ORDER BY ID ASC";
		 List<ShowTableConfig> tableConfigs = getJdbcTemplate().query(sql,new RowMapperFactory.ShowTableConfigRowMapper());
		 
		List<ShowDataConfig> showdataConfigs= findAllShowtableConfigs();
		 
		List<GridMenu> gridMenus= findAllGridMenus();
		 
		 if(tableConfigs !=null)
		 {
			 for(ShowTableConfig tc:tableConfigs)
			 {
				 
				 //加载 show data
				// tc.setShowDataConfigs(findShowDataByShowTableName(tc.getShowTableName()));
				 
				 for(ShowDataConfig sdf:showdataConfigs){
					 if(tc.getShowTableName().equals(sdf.getShowTableName())){
						 tc.getShowDataConfigs().add(sdf);
					 }
					 
				 }
				 
				 List<GridMenu> currentHasDealList=new ArrayList<GridMenu>();
				 for(GridMenu gm:gridMenus){
					 if(gm.getShowTableConfigId()==tc.getId()){
						 if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_TOOLBAR)
						 {
							 tc.getToolBarMenus().add(gm);
							 
						 }else  if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_GRID_FRONT)
						 {
							 tc.getRowFrontMenus().add(gm);
							 
						 }
						 else  if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_GRID_BACK)
						 {
							 tc.getRowBackMenus().add(gm);
							 
						 }
						 else  if(gm.getMenuType()==GridMenu.KEY_GRIDMENU_MENU_TYPE_GRID_CONTEXT_MENU)
						 {
							 tc.getContextMenus().add(gm);
							 
						 }
						 
						 currentHasDealList.add(gm);
					 }
				 }
				 
				 gridMenus.removeAll(currentHasDealList);//从集合中移除 
				 
				 
				 
				 
			 }
			 
			 
		 }
		return tableConfigs;
	}
	
	
	public List<ShowDataConfig> findShowDataByShowTableName(String showTableName)
	{
		String sql ="select * from v_showdata_config where showtable_name=? and is_load=1  order by sort ASC";
		
		return getJdbcTemplate().query(sql, new Object[]{showTableName}, new RowMapperFactory.ShowDataConfigRowMapper());
		
	}
	
	
	public List<GridMenu> findGridMenuByShowTableConfigId(long showTableConfigId)
	{
		String sql ="select * from v_gridmenus where showtable_config_id=? order by sort ASC";
		
		return getJdbcTemplate().query(sql, new Object[]{showTableConfigId}, new RowMapperFactory.GridMenuRowMapper());
	}

	@Override
	public List<Constants> loadConstantsCache() {
		List<Constants> list = getJdbcTemplate().query("select * from v_constants",new RowMapperFactory.ConstantsRowMapper());
		return list;
	}

	/* (non-Javadoc)
	 * @see cn.finder.wae.business.module.sys.dao.SysDao#loadServiceInterfaceCache()
	 */
	@Override
	public List<ServiceInterface> loadServiceInterfaceCache() {
		List<ServiceInterface> list = getJdbcTemplate().query("select * from v_service_interface",new RowMapperFactory.ServiceInterfaceRowMapper());
		return list;
	}

	@Override
	public List<UserConfig> loadUserConfigCache() {
		// TODO Auto-generated method stub
		List<UserConfig> list = getJdbcTemplate().query("select * from v_user_config",new RowMapperFactory.UserConfigRowMapper());
		return list;
	}
	
	
	/***
	 * 获取所有的列配置信息
	 * @return
	 */
	public List<ShowDataConfig> findAllShowtableConfigs()
	{
		String sql ="select * from v_showdata_config where is_load=1  order by sort ASC";
		return getJdbcTemplate().query(sql, new RowMapperFactory.ShowDataConfigRowMapper());
	}
	
	/***
	 * 获取所有的Gridmenus
	 * @return
	 */
	public List<GridMenu> findAllGridMenus()
	{
		String sql ="select * from v_gridmenus  order by sort ASC";
		return getJdbcTemplate().query(sql, new RowMapperFactory.GridMenuRowMapper());
	}

	@Override
	public List<PageIndex> loadPageIndexCache() {
		List<PageIndex> list = getJdbcTemplate().query("select * from wae_t_pageindex",new RowMapperFactory.PageIndexMapper());
		return list;
	}

}
