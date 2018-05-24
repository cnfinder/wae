package cn.finder.wae.wx.cache;


public class WXCache {

	private static WXCache instance;
	
	private static WXSessionCache wXSessionCache;
	public static WXCache getInstance(){
		if(instance == null)
			instance = new WXCache(0);
		return instance;
	}
	
	public WXCache(int size)
	{
		wXSessionCache=new WXSessionCache();
		
		
		
	}
	
	public WXSessionCache getWXSessionCache(){
		return wXSessionCache;
	}

}
