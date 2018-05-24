package cn.finder.wae.business.domain;

import java.util.Date;

/***
 * 被 t_shwotable_config引用
 * @author wu hualong
 *
 */
public class GridMenu {

	public final static int KEY_GRIDMENU_MENU_TYPE_TOOLBAR=41;
	
	public final static int KEY_GRIDMENU_MENU_TYPE_GRID_FRONT=42;
	
	public final static int KEY_GRIDMENU_MENU_TYPE_GRID_BACK=43;
	public final static int KEY_GRIDMENU_MENU_TYPE_GRID_CONTEXT_MENU=44;
	
	private long id;
	private String name;
	private String command;
	private int menuType;
	private int sort;
	private long showTableConfigId;
	
	private int isAuth;
	
	private String iconCls;
	
	private Date createDate;
	private Date updateDate;
	
	private int columnType;
	
	private String remark;
	
	private int width;
	
	private int height;
	
	private String extSetting;
	
	private String cellCmdRender;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public long getShowTableConfigId() {
		return showTableConfigId;
	}
	public void setShowTableConfigId(long showTableConfigId) {
		this.showTableConfigId = showTableConfigId;
	}
	public int getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getMenuType() {
		return menuType;
	}
	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	public int getColumnType() {
		return columnType;
	}
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getExtSetting() {
		return extSetting;
	}
	public void setExtSetting(String extSetting) {
		this.extSetting = extSetting;
	}
	public String getCellCmdRender() {
		return cellCmdRender;
	}
	public void setCellCmdRender(String cellCmdRender) {
		this.cellCmdRender = cellCmdRender;
	}
	
	
	
	
	
	
}
