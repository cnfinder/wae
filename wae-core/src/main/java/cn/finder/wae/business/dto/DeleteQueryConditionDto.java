package cn.finder.wae.business.dto;

import java.util.HashMap;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;

public class DeleteQueryConditionDto extends QueryCondition<Object[]> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1848562802187073749L;

	/**
	 * 删除的 showtableConfigId
	 * 
	 * 会根据此参数获取到主键字段
	 * 删除配置规则
	 * 1. 主键字段一定是整型
	 * 2. 删除的 showtable_name 应该和 显示配置的showtable_name一致
	 * 3. 主键字段需要有对应的field_tablename表名
	 *
	 */
	private long showtableConfigId;
	
	private Map<String,Object> values=new HashMap<String,Object>();
	
	public void put(String key,Object value){
		values.put(key, value);
	}
	public Object get(String key){
		return values.get(key);
	}
	
	/**
	 * 客户端需要获取到 主键对应的值
	 * 
	 */
	private String ids[];

	public long getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
	
	
	
}
