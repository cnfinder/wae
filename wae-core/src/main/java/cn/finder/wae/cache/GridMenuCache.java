package cn.finder.wae.cache;

import java.util.List;

import cn.finder.wae.business.domain.GridMenu;
import cn.finder.wae.common.cache.BaseCache;


/***
 * key-
 * 
 * @author wu hualong
 *
 */
public class GridMenuCache extends BaseCache<Integer, List<GridMenu>>{
	
	/*******************名称常量 ********************/
	
	public final static int KEY_GRIDMENU_MENU_TYPE_TOOLBAR=1;
	
	public final static int KEY_GRIDMENU_MENU_TYPE_GRID_FRONT=2;
	
	public final static int KEY_GRIDMENU_MENU_TYPE_GRID_BACK=3;
}
