package cn.finder.wae.common.type;

import java.util.HashMap;


/**
 * @author: wuhualong
 * @data:2014-8-11下午3:27:00
 * @function:
 */
public class FinderMap<K,V>  extends  HashMap<K,V>{

	
	
	public void setValue(K key,V value){
		
		if(this.containsKey(key)){
			this.remove(key);
		}
		this.put(key, value);
	}
	
	public int getInt(K key){
		String v = this.get(key).toString();
		return Integer.valueOf(v);
	}
	
	public String getString(K key){
		return (String)this.get(key);
	}
	public float getFloat(K key){
		String v = this.get(key).toString();
		return Float.valueOf(v);
	}
	
	
	
}
