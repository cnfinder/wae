package cn.finder.wae.cache;

import java.util.HashMap;

import cn.finder.wae.business.domain.UserConfig;
import cn.finder.wae.business.domain.wx.WXCommand;

public class WXCommandCache {

	

	public final static String CACHE_KEY="cache_WXCommand";
	
	private static Cache<String,String, WXCommand> cache;
	
	public void setCache(Cache<String,String, WXCommand> cache){
		WXCommandCache.cache=cache;
	}
	
	
	public Cache<String,String, WXCommand> getCache(){
		return WXCommandCache.cache;
	}


	public boolean add(String key, WXCommand value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,WXCommand> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, WXCommand newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public WXCommand get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
}
