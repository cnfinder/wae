package cn.finder.wae.service.rest.cache;

import java.util.HashMap;

import cn.finder.wae.business.domain.User;
import cn.finder.wae.cache.Cache;
import cn.finder.wae.cache.DefaultCacheInterface;

public class UserLoginedCache extends DefaultCacheInterface<String, User>{

	public final static String CACHE_KEY="cache_userLogined";
	
	private static UserLoginedCache instance;
	
	private static Cache<String,String, User> cache;
	
	public void setCache(Cache<String,String, User> cache){
		UserLoginedCache.cache=cache;
	}
	
	public static UserLoginedCache getInstance(){
		if(instance==null){
			instance=new UserLoginedCache();
		}
		return instance;
	}

	@Override
	public boolean add(String key, User value) {
		// TODO Auto-generated method stub
		return cache.add(CACHE_KEY,key, value);
	}

	@Override
	public User get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY, key);
	}

	@Override
	public HashMap<String, User> get() {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY);
	}

	

	@Override
	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}

	@Override
	public boolean replace(String key, User newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}

	@Override
	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}

	@Override
	public boolean add(String key, User value, long expire) {
		// TODO Auto-generated method stub
		return cache.add(CACHE_KEY,key, value, expire);
	}
	
	
}
