package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.Role;

public class RoleCache {
	
	
public final static String CACHE_KEY="cache_role";
	
	private static Cache<String,Long, Role> cache;
	
	public void setCache(Cache<String,Long, Role> cache){
		RoleCache.cache=cache;
	}
	
	public Cache<String,Long, Role> getCache(){
		return RoleCache.cache;
	}

	//get menu by roleId
	/*
	 1. get all role ids from t_role
	 2. get menu list by every id from t_role_menu table
	 */
	
	public void getRoleCache(){
	}
	
	public Map<Long, Role> getBaseDatas() {
		return get();
	}
	
	public HashMap<Long,Role> get(){
		return cache.get(CACHE_KEY);
	}
	//
	
	

	public boolean add(Long key, Role value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<Long,Role> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(Long key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(Long key, Role newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(Long key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public Role get(Long key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
}
