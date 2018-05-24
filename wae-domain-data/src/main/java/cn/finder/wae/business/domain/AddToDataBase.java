package cn.finder.wae.business.domain;

import java.util.List;
import java.util.Map;



public class AddToDataBase {
	private Long showTableConfigId;
	private String tableName;   
	private Map<String, Object> fieldNameValues;   //用来存放列的键值对
	private String sql;
	private List<Object> values;
	
//	public int init(){ //初始化方法
//		StringBuffer sqlBuffer = new StringBuffer();
//		sqlBuffer.append("insert into" + tableName +" values(");
//		
//		Set<String> keys = fieldNameValues.keySet();
//		
//		for(Iterator<String>  it = keys.iterator(); it.hasNext();){
//			String key = it.next();
//			Object value = fieldNameValues.get(key);
//			
//		}
//		return 0;
//	}
	public Long getShowTableConfigId() {
		return showTableConfigId;
	}
	public void setShowTableConfigId(Long showTableConfigId) {
		this.showTableConfigId = showTableConfigId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Map<String, Object> getFieldNameValues() {
		return fieldNameValues;
	}
	public void setFieldNameValues(Map<String, Object> fieldNameValues) {
		this.fieldNameValues = fieldNameValues;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
	
	
}
