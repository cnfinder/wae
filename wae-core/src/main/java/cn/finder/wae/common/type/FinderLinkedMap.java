package cn.finder.wae.common.type;

import java.util.LinkedHashMap;




/**
 * @author: wuhualong
 * @data:2014-8-11下午3:27:00
 * @function:
 */
public class FinderLinkedMap<K,V>  extends  LinkedHashMap<K, V>{

	
	
	public void setValue(K key,V value){
		
		if(this.containsKey(key)){
			this.remove(key);
		}
		this.put(key, value);
	}
	
	public int getInt(K key){
		return (Integer)this.get(key);
	}
	
	public String getString(K key){
		return (String)this.get(key);
	}
	public float getFloat(K key){
		return (Float)this.get(key);
	}
	
	
	
}
