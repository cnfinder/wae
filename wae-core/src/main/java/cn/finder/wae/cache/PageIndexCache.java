package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.business.domain.PageIndex;

public class PageIndexCache {
public final static String CACHE_KEY="cache_PageIndex";
	
	private static Cache<String,String, PageIndex> cache;
	
	public void setCache(Cache<String,String, PageIndex> cache){
		PageIndexCache.cache=cache;
	}
	
	public Cache<String,String, PageIndex> getCache(){
		return PageIndexCache.cache;
	}
	public Map<String, PageIndex> getBaseDatas() {
		return get();
	}
	
	public HashMap<String,PageIndex> get(){
		return cache.get(CACHE_KEY);
	}



	public boolean add(String key, PageIndex value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,PageIndex> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, PageIndex newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public PageIndex get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
	
}
