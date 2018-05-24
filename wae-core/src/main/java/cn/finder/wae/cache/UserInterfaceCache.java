package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.business.domain.UserInterface;

public class UserInterfaceCache{

public final static String CACHE_KEY="cache_UserInterface";
	
	private static Cache<String,String, UserInterface> cache;
	
	public void setCache(Cache<String,String, UserInterface> cache){
		UserInterfaceCache.cache=cache;
	}
	
	public Cache<String,String, UserInterface> getCache(){
		return UserInterfaceCache.cache;
	}


	
	public Map<String, UserInterface> getBaseDatas() {
		return get();
	}
	
	public HashMap<String,UserInterface> get(){
		return cache.get(CACHE_KEY);
	}
	

	public boolean add(String key, UserInterface value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,UserInterface> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, UserInterface newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public UserInterface get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
}
