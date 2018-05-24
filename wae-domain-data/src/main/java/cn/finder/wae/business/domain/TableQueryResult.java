package cn.finder.wae.business.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import cn.finder.ui.webtool.QueryResult;


/***
 * 表格的数据返回
 * @author wu hualong
 *
 *Map<String,Object> 表示每条记录字段对应的值
 *Key-字段英文名 Object对应 值
 */
@XmlRootElement
@XmlSeeAlso({Map.class})
public class TableQueryResult extends QueryResult<Map<String,Object>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7004150454526664808L;
	/***
	 * 返回显示字段名称,用于表格 Header显示
	 */
	private List<ShowDataConfig> fields;
	
	
	private Map<String,String> statistics;
	
	//private List<GridMenu> toolbarGridMenus;
	
	//private List<GridMenu> rowFrontGridMenus;
	
	//private List<GridMenu> rowBackGridMenus;
	
	private ShowTableConfig showTableConfig;
	
	private  Message message=new Message();
	
	
	public TableQueryResult() {
		super();
		this.statistics = new HashMap<String, String>();
	}
	public List<ShowDataConfig> getFields() {
		return fields;
	}
	public void setFields(List<ShowDataConfig> fields) {
		this.fields = fields;
	}
	public Map<String, String> getStatistics() {
		return statistics;
	}
	public void setStatistics(Map<String, String> statistics) {
		this.statistics = statistics;
	}
	public ShowTableConfig getShowTableConfig() {
		return showTableConfig;
	}
	public void setShowTableConfig(ShowTableConfig showTableConfig) {
		this.showTableConfig = showTableConfig;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	
	
	
}
