package cn.finder.wae.business.dto;

import java.util.HashMap;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;

public class MapParaQueryConditionDto<K,V> extends QueryCondition<Object[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3572219798927066055L;

	private Map<K, V>  mapParas;//存储 通过 data:{p1:1} 
	
	// 参数 传递  Object为数组
	private Map<String,Object>  forwardParams=new HashMap<String, Object>(); //原生数据,可以传递到 handlerclass

	public MapParaQueryConditionDto() {
		super();
		this.mapParas = new HashMap<K, V>();
	}



	public Map<K, V> getMapParas() {
		return mapParas;
	}



	public void setMapParas(Map<K, V> mapParas) {
		this.mapParas = mapParas;
	}


	public void put(K key,V value)
	{
		mapParas.put(key, value);
	}
	
	public V get(K key)
	{
		return mapParas.get(key);
	}

	public Map<String, Object> getForwardParams() {
		return forwardParams;
	}

	public void setForwardParams(Map<String, Object> forwardParams) {
		this.forwardParams = forwardParams;
	}
	
}
