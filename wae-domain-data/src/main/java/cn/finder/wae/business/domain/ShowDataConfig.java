package cn.finder.wae.business.domain;

import java.util.HashMap;
import java.util.Map;

public class ShowDataConfig extends BaseDomain{

	private long id;
	
	private String showTableName;
	
	private String fieldName;
	private String fieldNameAlias;
	
	private int editable;
	
	private int showType;
	
	private int sort;
	
	private String parentSelectSql;
	
	private String parentTableName;
	
	private String parentTableKey;
	
	private String parentShowField;
	
	private int isHide;
	private int width;
	
	private String statistics;
	
	private int isLoad;
	
	
	
	private int allowSort;
	
	private int dataType;
	private String groupName;
	private String parentGroupName;
	private String fieldNav;
	
	private String format;

	
	private String itemRenderEvent;
	
	private int isShowSearch;
	
	private String fieldTableName;
	
	
	private String defaultValue;
	
	private Object oldValue;   //用来存储被外键引用替换的数据，23类型   用的时候要将之前的值清空
	private String oldText;    //用来存储外键显示字段 40类型     用的时候要将之前的值清空
	
	private String tipText;
	
	private String validateTip;
	
	private String validateRule;
	
	private int isCrypt;
	
	private int height;
	
	private int isPrimaryKey;
	
	private String parentControlId;
	
	private int isMergeColumn;
	
	private ShowDataType showDataType;
	
	private String extProperties; //扩展属性  key1=value1;key2=value2
	
	
	private int lockField;
	
	public String getFieldTableName() {
		return fieldTableName;
	}

	public void setFieldTableName(String fieldTableName) {
		this.fieldTableName = fieldTableName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getShowTableName() {
		return showTableName;
	}

	public void setShowTableName(String showTableName) {
		this.showTableName = showTableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldNameAlias() {
		return fieldNameAlias;
	}

	public void setFieldNameAlias(String fieldNameAlias) {
		this.fieldNameAlias = fieldNameAlias;
	}

	public int getEditable() {
		return editable;
	}

	public void setEditable(int editable) {
		this.editable = editable;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getParentSelectSql() {
		return parentSelectSql;
	}

	public void setParentSelectSql(String parentSelectSql) {
		this.parentSelectSql = parentSelectSql;
	}

	public String getParentTableName() {
		return parentTableName;
	}

	public void setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
	}


	public String getParentShowField() {
		return parentShowField;
	}

	public void setParentShowField(String parentShowField) {
		this.parentShowField = parentShowField;
	}

	public int getIsHide() {
		return isHide;
	}

	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getStatistics() {
		return statistics;
	}

	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}

	public int getIsLoad() {
		return isLoad;
	}

	public void setIsLoad(int isLoad) {
		this.isLoad = isLoad;
	}

	public String getParentTableKey() {
		return parentTableKey;
	}

	public void setParentTableKey(String parentTableKey) {
		this.parentTableKey = parentTableKey;
	}

	public int getAllowSort() {
		return allowSort;
	}

	public void setAllowSort(int allowSort) {
		this.allowSort = allowSort;
	}

	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	public String getFieldNav() {
		return fieldNav;
	}

	public void setFieldNav(String fieldNav) {
		this.fieldNav = fieldNav;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getItemRenderEvent() {
		return itemRenderEvent;
	}

	public void setItemRenderEvent(String itemRenderEvent) {
		this.itemRenderEvent = itemRenderEvent;
	}

	public int getIsShowSearch() {
		return isShowSearch;
	}

	public void setIsShowSearch(int isShowSearch) {
		this.isShowSearch = isShowSearch;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getTipText() {
		return tipText;
	}

	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

	public String getValidateTip() {
		return validateTip;
	}

	public void setValidateTip(String validateTip) {
		this.validateTip = validateTip;
	}

	public String getValidateRule() {
		return validateRule;
	}

	public void setValidateRule(String validateRule) {
		this.validateRule = validateRule;
	}

	public int getIsCrypt() {
		return isCrypt;
	}

	public void setIsCrypt(int isCrypt) {
		this.isCrypt = isCrypt;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getIsPrimaryKey() {
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(int isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public String getParentControlId() {
		return parentControlId;
	}

	public void setParentControlId(String parentControlId) {
		this.parentControlId = parentControlId;
	}

	public String getOldText() {
		return oldText;
	}

	public void setOldText(String oldText) {
		this.oldText = oldText;
	}

	public int getIsMergeColumn() {
		return isMergeColumn;
	}

	public void setIsMergeColumn(int isMergeColumn) {
		this.isMergeColumn = isMergeColumn;
	}

	public ShowDataType getShowDataType() {
		return showDataType;
	}

	public void setShowDataType(ShowDataType showDataType) {
		this.showDataType = showDataType;
	}

	public String getExtProperties() {
		return extProperties;
	}

	public void setExtProperties(String extProperties) {
		this.extProperties = extProperties;
	}

		public int getLockField() {
		return lockField;
	}

	public void setLockField(int lockField) {
		this.lockField = lockField;
	}
	public Map<String,Object> findExtPropertiesMap()
	{
		Map<String,Object> item = new HashMap<String,Object>();
		if(extProperties==null){
			
		}
		else{
		    String[]  arr1 =extProperties.split(";");
		    
		    for(String s:arr1){
		    	String[] s1_arr = s.split("=");
		    	item.put(s1_arr[0], s1_arr[1]);
		    }
		}
		return item;
	}


	

	
	

}
