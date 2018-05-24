package cn.finder.wae.wx.cache;

import java.util.HashMap;

import cn.finder.wae.cache.Cache;
import cn.finder.wae.cache.DefaultCacheInterface;

public class WXSessionCache extends DefaultCacheInterface<String, HashMap<String,Object>>{
	public final static String CACHE_KEY="cache_WXSession";
	
	private static Cache<String,String, HashMap<String,Object>> cache;
	
	public void setCache(Cache<String,String, HashMap<String,Object>> cache){
		WXSessionCache.cache=cache;
	}
	
	


	public boolean add(String key, HashMap<String,Object> value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,HashMap<String,Object>> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, HashMap<String,Object> newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean constains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public HashMap<String,Object> get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}




	@Override
	public boolean add(String key, HashMap<String, Object> value, long expire) {
		return cache.add(CACHE_KEY, key, value,expire);
	}




	@Override
	public boolean add(HashMap<String, HashMap<String, Object>> value,
			long expire) {
		return cache.add(CACHE_KEY,value,expire);
	}
	
	
	
}
