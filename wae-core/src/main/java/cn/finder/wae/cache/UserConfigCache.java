package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.UserConfig;

public class UserConfigCache {

	public final static String CACHE_KEY="cache_UserConfig";
	
	private static Cache<String,String, UserConfig> cache;
	
	public void setCache(Cache<String,String, UserConfig> cache){
		UserConfigCache.cache=cache;
	}
	
	public Cache<String,String, UserConfig> getCache(){
		return UserConfigCache.cache;
	}

	public Map<String, UserConfig> getBaseDatas() {
		return get();
	}
	
	public HashMap<String,UserConfig> get(){
		return cache.get(CACHE_KEY);
	}

	public boolean add(String key, UserConfig value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,UserConfig> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, UserConfig newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public UserConfig get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
}
