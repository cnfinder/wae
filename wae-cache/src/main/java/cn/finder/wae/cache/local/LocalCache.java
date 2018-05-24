package cn.finder.wae.cache.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.finder.wae.cache.BaseCache;
/**
 *  本地缓存实现
 * {@value this is local base cache all cache should extends BaseCache}
 * @author finder
 * @param <C> cache_key 不需要
 * @param <K> key
 * @param <V> value
 */
public class LocalCache<C,K,V> extends BaseCache<C,K,V>{

	
	
	protected LinkedHashMap<C,HashMap<K,V>> baseDatas=null; //对应 redis 的 hash存储
	
	public LocalCache()
	{
		baseDatas=new LinkedHashMap<C, HashMap<K,V>>();
	}
	

	public Map<K, V> getBaseDatas(C cache_key) {
		return baseDatas.get(cache_key);
	}

	
	

	public boolean add(C cache_key, K key, V value) {
		return add(cache_key,key,value,0);
	}

	
	public boolean add(C cache_key, K key, V value, long expire) {
		HashMap<K,V> mapdata=new HashMap<K, V>();
		mapdata.put(key, value);
		add(cache_key,mapdata,expire);
		return true;
	}


	public boolean add(C cache_key, HashMap<K, V> value, long expire) {
		if(!baseDatas.containsKey(cache_key)){
			baseDatas.put(cache_key, value);
		}
		else{
			HashMap<K, V> orig_values=baseDatas.get(cache_key);
			
			Set<Entry<K,V>>  theset=value.entrySet();
			for(Entry<K,V> entry:theset){
				orig_values.put(entry.getKey(), entry.getValue());
			}
			
			
		}
		return true;
	}

	public boolean add(C cache_key, HashMap<K, V> value) {
		return add(cache_key,value,0);
	}

	public  V get(C cache_key, K key) {
		if(baseDatas.containsKey(cache_key)){
			return baseDatas.get(cache_key).get(key);
		}
		else{
			return null;
		}
	}

	public HashMap<K,V> get(C cache_key) {
		
		HashMap<K,V> thedata=baseDatas.get(cache_key);
		if(thedata==null){
			thedata=new HashMap<K,V>();
		}
		return thedata;
	}

	

	public List<V> get(C cache_key, long startIndex, long endIndex) {
		List<V> retData=new ArrayList<V>();
		if(baseDatas.containsKey(cache_key)){
			
			HashMap<K, V> map =baseDatas.get(cache_key);
			Collection<V> values= map.values();
			
			if(values!=null){
				Iterator<V> iter= values.iterator();
				int i=0;
				while(iter.hasNext()){
					if(i>=startIndex && i<endIndex){
						retData.add(iter.next());
					}
					i++;
				}
				
			}
			
		}
		return retData;
	}

	

	public boolean remove(C cache_key, K key) {
		if(baseDatas.containsKey(cache_key)){
			baseDatas.remove(key);
			return true;
		}
		return false;
	}

	public boolean replace(C cache_key, K key, V newValue) {
		if(baseDatas.containsKey(cache_key)){
			baseDatas.get(cache_key).put(key, newValue);
		}else{
			this.add(cache_key, key, newValue);
		}
		return true;
	}


	public List<V> get(C cache_key, K... key) {
		List<V> retData=new ArrayList<V>();
		if(baseDatas.containsKey(cache_key)){
			
			HashMap<K, V> map =baseDatas.get(cache_key);
			Collection<V> values= map.values();
			
			if(values!=null){
				for(V v:values){
					retData.add(v);
				}
			}
			
		}
		return retData;
	}



	public boolean contains(C cache_key) {
		// TODO Auto-generated method stub
		return this.baseDatas.containsKey(cache_key);
	}


	public boolean contains(C cache_key, K key) {
		
		if(baseDatas.containsKey(cache_key)){
			HashMap<K, V> map =baseDatas.get(cache_key);
			return map.containsKey(key);
		}
		return false;
	}


	public boolean clear(C cache_key) {
		baseDatas.remove(cache_key);
		return true;
	}


	public boolean clear() {
		baseDatas.clear();
		return true;
	}



	
	
	
	
}
