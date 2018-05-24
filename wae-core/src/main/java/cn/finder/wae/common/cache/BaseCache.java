package cn.finder.wae.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@value this is base cache all cache should extends BaseCache}
 * @author dragon
 *
 * @param <K> key
 * @param <V> value
 */
@Deprecated
public class BaseCache<K,V> {

	private int size;
	
	protected LinkedHashMap<K,V> baseDatas=null;
	
	public BaseCache()
	{
		//default cache size 0    表示自动大
		this(0);
	}
	
	public BaseCache(int size){
		this.size=size;
		baseDatas=new LinkedHashMap<K, V>(size);
	}
	
	

	public Map<K, V> getBaseDatas() {
		return baseDatas;
	}

	
	public void add(K key,V value)
	{
		if(!baseDatas.containsKey(key)){
			//remove first
			arrangeCache();
			
			//add last
			baseDatas.put(key, value);
			
		}
	}
	
	private void arrangeCache()
	{
		if(size>0)
		{
			if(baseDatas.size()>=size)
			{
				//remove this first
				//baseDatas.
				Set<K> keys=baseDatas.keySet();
				K k = null;
				for(K s:keys)
				{
					k=s;
					break;
				}
				baseDatas.remove(k);
			}
		}
	}
	
	
	public void remove(K key)
	{
		if(baseDatas.containsKey(key)){
			baseDatas.remove(key);
		}
	}
	
	public void clear()
	{
		baseDatas.clear();
	}
	
	
	public void replace(K key,V newValue)
	{
		/*
		if(baseDatas.containsKey(key))
		{
			baseDatas.remove(key);
		}*/
		//this.add(key, newValue);
		// if key is exists,old value will be replaced
		baseDatas.put(key, newValue);
	}
	
	public V get(K key)
	{
		if(baseDatas.containsKey(key)){
			return baseDatas.get(key);
		}
		else{
			return null;
		}
	}
	
	public boolean constains(K key){
		return baseDatas.containsKey(key);
	}
	
}
