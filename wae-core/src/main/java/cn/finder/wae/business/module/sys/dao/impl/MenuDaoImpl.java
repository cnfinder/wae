package cn.finder.wae.business.module.sys.dao.impl;

import java.util.ArrayList;
import java.util.List;

import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.RoleMenu;
import cn.finder.wae.business.module.sys.dao.MenuDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class MenuDaoImpl extends BaseJdbcDaoSupport implements MenuDao {

	@Override
	public Menu findMenuById(Long menuId) {
		String sql = "select * from v_menu where id = ?";
		return  queryForSingle(sql, new Object[]{menuId},new RowMapperFactory.MenuRowMapper());
	}

//	@Override
//	public MenuNode generateRootNode() {
//		MenuNode mNode = new MenuNode();
//		Menu menu = this.findMenuById(10000l);
//		List<Menu> menus = this.findAll();
//		mNode = generateNode(menu, menus);
//		
//		return mNode;
//	}

	@Override
	public List<Menu> findAll() {
		String sql = "select * from v_menu";
		List<Menu> list = getJdbcTemplate().query(sql, new RowMapperFactory.MenuRowMapper());
		return list;
	}
	
	@Override
	public Menu generateMenuTree(Menu menu, List<Menu> menus){
		if(menu.getChildren() == null){
			menu.setChildren(new ArrayList<Menu>());
		}
		for(Menu childMenu : menus){
			if(childMenu.getParentId().longValue() == menu.getId().longValue()){
				childMenu = generateMenuTree(childMenu, menus);
				if(childMenu != null){
					childMenu.setParent(menu);
					menu.getChildren().add(childMenu);
				}
			}
		}
		return menu;
	}
	
	
	@Override
	public List<Menu> generateTree(List<Menu> menus){
		List<Menu> highest = new ArrayList<Menu>();
		if(menus != null && menus.size() > 0){
			highest = this.findHighestMenu(menus);
			for(Menu menu : highest){
				this.generateMenuTree(menu, menus);
			}
			return highest;
		}
		return null;
	}
	
	public List<Menu> findHighestMenu(List<Menu> menus){
		List<Menu> highest= new ArrayList<Menu>();
		for(Menu menu : menus){
			if(menu.getParentId().longValue() == 0l){
				highest.add(menu);
			}else{
				//暂时不用
			}
		}
		return highest;
		
	}
	
	
	public List<Menu> findMenusByRoleId(Long roleId) {
		String sql = "select m.* from t_role_menu rm join t_menu m on rm.menu_id=m.id where role_id=? order by m.sort asc";
		List<Menu> list = getJdbcTemplate().query(sql, new Object[]{roleId}, new RowMapperFactory.MenuRowMapper());
		return list;
	}
	
	public List<RoleMenu> findAllRoleMenus() {
		String sql = "select rm.*,m.command,m.name,m.parent_id,m.level,m.is_enable,m.create_date,m.update_date,m.style,m.style_hover,m.sort,m.is_auth,m.type,m.icon_position,m.remark from t_role_menu rm join t_menu m on rm.menu_id=m.id order by m.sort asc";
		List<RoleMenu> list = getJdbcTemplate().query(sql,  new RowMapperFactory.RoleMenuRowMapper());
		return list;
	}

	@Override
	public List<Menu> findPrivilegeMenusByUserId(String userId) {
		String sql="select m.* from wae_t_privilege pri join t_menu m on pri.menu_id=m.id where user_id=? order by m.sort asc";
		List<Menu> list = getJdbcTemplate().query(sql, new Object[]{userId}, new RowMapperFactory.MenuRowMapper());
		
		return list;
	}
	

	
	
	
}
