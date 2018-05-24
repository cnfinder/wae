package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.Map;

import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.ServiceInterface;

/**
 * @author: wuhualong
 * @data:2014-4-16上午8:57:39
 * @function: 服务接口 配置缓存
 */
public class ServiceInterfaceCache{

	public final static String CACHE_KEY="cache_ServiceInterface";
	
	private static Cache<String,String, ServiceInterface> cache;
	
	public void setCache(Cache<String,String, ServiceInterface> cache){
		ServiceInterfaceCache.cache=cache;
	}
	
	public Cache<String,String, ServiceInterface> getCache(){
		return ServiceInterfaceCache.cache;
	}

	
	public Map<String, ServiceInterface> getBaseDatas() {
		return get();
	}
	
	public HashMap<String,ServiceInterface> get(){
		return cache.get(CACHE_KEY);
	}


	public boolean add(String key, ServiceInterface value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<String,ServiceInterface> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(String key, ServiceInterface newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public ServiceInterface get(String key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
	
	
	
}
