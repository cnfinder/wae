package cn.finder.wae.business.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 */

public class Menu implements java.io.Serializable {

	// Fields

	private Long id;
	private String command;
	private String name;
	private Long parentId;
	private Integer level;
	private Date createDate;
	private Date updateDate;
	private Integer isEnable;
	private Long type;
	
	private Menu parent;
	private List<Menu>	children;
	// Constructors
	
	private String style;
	
	private String styleHover;
	
	
	private String text;
	
	private String iconCls;
	
	private String iconPosition;
	
	private int sort;
	

	/** default constructor */
	public Menu() {
	}

	/** minimal constructor */
	public Menu(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Menu(Long id, String command, String name, Long parentId,
			Integer level, Timestamp createDate, Timestamp updateDate,
			Integer isEnable, Long type) {
		this.id = id;
		this.command = command;
		this.name = name;
		this.parentId = parentId;
		this.level = level;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.isEnable = isEnable;
		this.type = type;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getText() {
		text = name;
		return name;
	}

	public String getIconCls() {
		
		this.iconCls = style;
		return iconCls;
	}

	public String getIconPosition() {
		return iconPosition;
	}
	

	public void setIconPosition(String iconPosition) {
		this.iconPosition = iconPosition;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleHover() {
		return styleHover;
	}

	public void setStyleHover(String styleHover) {
		this.styleHover = styleHover;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	
	
}