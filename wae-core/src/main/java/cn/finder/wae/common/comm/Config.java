package cn.finder.wae.common.comm;

import java.io.Serializable;


public class Config implements Serializable{
	
	
	private static final long serialVersionUID = 6430561247373676128L;
	private boolean userCache;
	
	private int rawCacheSize;

	
	private boolean rawCircleEnable;
	private long rawCircleTime;
	
	public boolean isUserCache() {
		return userCache;
	}

	public void setUserCache(boolean userCache) {
		this.userCache = userCache;
	}

	public int getRawCacheSize() {
		return rawCacheSize;
	}

	public void setRawCacheSize(int rawCacheSize) {
		this.rawCacheSize = rawCacheSize;
	}

	public long getRawCircleTime() {
		return rawCircleTime;
	}

	public void setRawCircleTime(long rawCircleTime) {
		this.rawCircleTime = rawCircleTime;
	}

	public boolean isRawCircleEnable() {
		return rawCircleEnable;
	}

	public void setRawCircleEnable(boolean rawCircleEnable) {
		this.rawCircleEnable = rawCircleEnable;
	}

	
}
