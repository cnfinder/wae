package cn.finder.wae.business.module.sys.dao;

import java.util.List;

import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.RoleMenu;

public interface MenuDao {
	public List<Menu> findAll();
	
	public Menu findMenuById(Long menuId);
	
	public Menu generateMenuTree(Menu menu, List<Menu> menus);

	public List<Menu> generateTree(List<Menu> menus);
	
	
	/***
	 * 获取所有的 Role 和 menu 对应记录,多-多
	 * @return
	 */
	public List<RoleMenu> findAllRoleMenus();
	
	
	/***
	 * 根据role id 获取菜单
	 * @param roleId
	 * @return
	 */
	public List<Menu> findMenusByRoleId(Long roleId);
	
	/***
	 * 根据用户获取私有菜单
	 * @param userId
	 * @return
	 */
	public List<Menu> findPrivilegeMenusByUserId(String userId);
	
}
