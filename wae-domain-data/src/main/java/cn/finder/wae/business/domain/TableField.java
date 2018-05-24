package cn.finder.wae.business.domain;

public class TableField {
	
	private String tableName;

	private String columnName;
	
	private String dataType;
	
	private Object defaultValue;
	
	private String columnKey;
	
	private int isPrimaryKey;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getIsPrimaryKey() {
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(int isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
		
	
}
