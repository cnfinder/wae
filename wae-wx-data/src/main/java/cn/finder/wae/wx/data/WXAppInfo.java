package cn.finder.wae.wx.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import cn.finder.wae.cache.Cache;
import cn.finder.wae.cache.DefaultCacheInterface;
import cn.finder.wx.response.FindAccessTokenResponse;
import cn.finder.wx.service.WXService;

/***
 * 获取 OPENID INFO 入口
 * 包含 服务号 企业号 AccessToken获取
 * @author whl
 *
 */
public class WXAppInfo {

	
	
	private static AppInfoCache  appInfos=new AppInfoCache();
	private static CorpTokenInfoCache  corpTokenInfos=new CorpTokenInfoCache();
	
	
	
	//平台中管理的微信 信息  Key=appid value=Appinfo
	//public static Map<String, AppInfo> appInfos=Collections.synchronizedMap(new HashMap<String, AppInfo>());
	
	
	//企业号所有管理权限 票据
	//public static Map<String,CorpTokenInfo> corpTokenInfos= Collections.synchronizedMap(new HashMap<String, CorpTokenInfo>());
	
	
	public static Map<String, AppInfo> getAppInfos(){
		
		return appInfos.get();
	}
	
	
	
	/***
	 * 根据appId获取获取app info
	 * @param appId
	 * @return
	 */
	public static AppInfo getAppInfo(String appid){
		
		AppInfo appinfo= appInfos.get(appid);
		
		if(appinfo.getEndExpires() <= new Date().getTime()){
			//重新加载
			WXService service=new WXService();
		    FindAccessTokenResponse resp=service.findAccessToken(appinfo.getGrantType(), appinfo.getAppid(), appinfo.getAppSecret());
			if(StringUtils.isEmpty(resp.getErrcode())){
				appinfo.setAccessToken(resp.getAccess_token());
		    	appinfo.setExpiresIn(resp.getExpires_in());
			}
	    	
		}
		
		return appinfo;
	}
	
	
	/***
	 * 根据公众号 wx_openid 获取获取app info
	 * 不推荐使用此方法， 尽量使用getAppInfo 传递appId参数调用
	 * 此方法适合在 回调接口中使用
	 * @param wx_openid
	 * @return
	 */
	public static AppInfo getAppInfoByOpenid(String wx_openid){
		AppInfo appinfo=null;
		
		Set<Entry<String,AppInfo>> entrySet= appInfos.get().entrySet();
		
		Iterator<Entry<String,AppInfo>> itr= entrySet.iterator();
		
		while(itr.hasNext()){
			Entry<String,AppInfo> entry=itr.next();
			//String key=entry.getKey();
			AppInfo ai=entry.getValue();
			
			if(ai.getOpenid().equalsIgnoreCase(wx_openid)){
				appinfo=ai;
				break;
			}
		}
		
		if(appinfo.getEndExpires() <= new Date().getTime()){
			//重新加载
			WXService service=new WXService();
		    FindAccessTokenResponse resp=	service.findAccessToken(appinfo.getGrantType(), appinfo.getAppid(), appinfo.getAppSecret());
			if(StringUtils.isEmpty(resp.getErrcode())){
				appinfo.setAccessToken(resp.getAccess_token());
		    	appinfo.setExpiresIn(resp.getExpires_in());
			}
	    	
		}
		
		return appinfo;
	}
	
	
	
	/***
	 * 更新服务号票据
	 * @param appId 
	 * @param appInfo 新票据
	 */
	public synchronized static void updateAppInfo(String appId,AppInfo appInfo){
		if(appInfos.contains(appId)){
			appInfos.remove(appId);
		}
		appInfos.add(appId, appInfo,appInfo.getExpiresIn());
		
	}
	//------------------------------------------------------------------------------------------------------
	
	/***
	 * 获取企业临时访问票据
	 * @param corpid
	 * @param corpsecret
	 * @return
	 */
	public static CorpTokenInfo getCorpAccessTokenInfo(String corpid,String corpsecret){
		CorpTokenInfo corpTokenInfo= corpTokenInfos.get(corpIdSecretGenerator(corpid,corpsecret));
		
		if(corpTokenInfo.getEndExpires() <= new Date().getTime()){
			//重新加载
			WXService.CorpService service=new WXService.CorpService();
		    cn.finder.wx.corp.response.FindAccessTokenResponse resp=service.findAccessToken(corpTokenInfo.getCorpid(),corpTokenInfo.getCorpsecret());
			if(StringUtils.isEmpty(resp.getErrcode())){
				corpTokenInfo.setAccessToken(resp.getAccess_token());
				corpTokenInfo.setExpiresIn(resp.getExpires_in());
			}
	    	
		}
		
		return corpTokenInfo;
	}
	
	
	
	/***
	 * 获取企业号超级管理权限临时访问票据
	 * @param corpid
	 * @return
	 */
	public static CorpTokenInfo getCorpAccessTokenInfo(String corpid){
		
	    Set<String> keys=corpTokenInfos.get().keySet();
	    
	    for(String key:keys){
	    	if(key.startsWith(corpid)){
	    		
	    		CorpTokenInfo corpTokenInfo=corpTokenInfos.get(key);
	    		
	    		if("SA".equalsIgnoreCase(corpTokenInfo.getAdminGroupTypeCode())){
	    			return getCorpAccessTokenInfo(corpTokenInfo.getCorpid(),corpTokenInfo.getCorpsecret());
	    		}
	    	}
	    }
	    return null;
	}
	
	
	
	
	/***
	 * 更新企业号票据
	 * @param corpid
	 * @param corpsecret
	 * @param corpTokenInfo 新的票据
	 */
	public synchronized static void updateCorpAccessTokenInfo(String corpid,String corpsecret,CorpTokenInfo corpTokenInfo){
		
		if(corpTokenInfos.get().containsKey(corpIdSecretGenerator(corpid,corpsecret))){
			corpTokenInfos.remove(corpIdSecretGenerator(corpid,corpsecret));
		}
		corpTokenInfos.add(corpIdSecretGenerator(corpid,corpsecret), corpTokenInfo,corpTokenInfo.getExpiresIn());
		
	}
	
	
	
	
	public static String corpIdSecretGenerator(String corpid,String corpsecret){
		return corpid+":"+corpsecret;
	}
	
	
	
	public static class AppInfoCache extends DefaultCacheInterface<String, AppInfo>{
		
		public final static String CACHE_KEY="cache_appInfo";
		
		private static Cache<String,String, AppInfo> cache;
		
		public void setCache(Cache<String,String, AppInfo> cache){
			AppInfoCache.cache=cache;
		}
		

		
		@Override
		public boolean add(String key, AppInfo value, long expire) {
			// TODO Auto-generated method stub
			return cache.add(CACHE_KEY,key, value, expire);
		}



		public boolean add(String key, AppInfo value) {
			return cache.add(CACHE_KEY, key,value);
		}

		public  boolean add(HashMap<String,AppInfo> value){
			return cache.add(CACHE_KEY, value);
		}


		public boolean remove(String key) {
			// TODO Auto-generated method stub
			return cache.remove(CACHE_KEY,key);
		}



		public boolean replace(String key, AppInfo newValue) {
			// TODO Auto-generated method stub
			return cache.replace(CACHE_KEY,key, newValue);
		}



		public boolean contains(String key) {
			// TODO Auto-generated method stub
			return cache.contains(CACHE_KEY,key);
		}


		public HashMap<String,AppInfo> get(){
			return cache.get(CACHE_KEY);
		}

		public AppInfo get(String key) {
			// TODO Auto-generated method stub
			return cache.get(CACHE_KEY,key);
		}


		


		public boolean clear() {
			// TODO Auto-generated method stub
			return cache.clear(CACHE_KEY);
		}


		
	}
	public static class CorpTokenInfoCache extends DefaultCacheInterface<String, CorpTokenInfo>{
		
		public final static String CACHE_KEY="cache_corpTokenInfo";
		private static Cache<String,String, CorpTokenInfo> cache;
		
		public void setCache(Cache<String,String, CorpTokenInfo> cache){
			CorpTokenInfoCache.cache=cache;
		}
		
		

		public boolean add(String key, CorpTokenInfo value) {
			return cache.add(CACHE_KEY, key,value);
		}

		public  boolean add(HashMap<String,CorpTokenInfo> value){
			return cache.add(CACHE_KEY, value);
		}


		public boolean remove(String key) {
			// TODO Auto-generated method stub
			return cache.remove(CACHE_KEY,key);
		}
		
		


		@Override
		public boolean add(String key, CorpTokenInfo value, long expire) {
			// TODO Auto-generated method stub
			return cache.add(CACHE_KEY,key, value, expire);
		}



		public boolean replace(String key, CorpTokenInfo newValue) {
			// TODO Auto-generated method stub
			return cache.replace(CACHE_KEY,key, newValue);
		}



		public boolean contains(String key) {
			// TODO Auto-generated method stub
			return cache.contains(CACHE_KEY,key);
		}




		public CorpTokenInfo get(String key) {
			// TODO Auto-generated method stub
			return cache.get(CACHE_KEY,key);
		}


		


		public boolean clear() {
			// TODO Auto-generated method stub
			return cache.clear(CACHE_KEY);
		}



		@Override
		public HashMap<String, CorpTokenInfo> get() {
			// TODO Auto-generated method stub
			return cache.get(CACHE_KEY);
		}




		
		
	}
	
	
}
