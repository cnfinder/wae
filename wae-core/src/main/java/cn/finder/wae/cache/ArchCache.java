package cn.finder.wae.cache;




/**
 * 全局静态缓存
 * @author wu hualong
 *
 */
public class ArchCache {

	
	private static ArchCache instance;
	
	//缓存数据  系统配置缓存
	private static SysConfigCache sysConfigCache;
	
	private static ConstantsCache constantsCache;
	
	
	private static ShowTableConfigCache showTableConfigCache;
	
	
	private static GridMenuCache gridMenuCache;
	
	private static RoleCache roleCache;
	
	private static MetaDataCache metaDataCache;
	
	private static DataImportCache dataImportCache;
	
	
	private static ServiceInterfaceCache serviceInterfaceCache;
	
	private static UserConfigCache userConfigCache;
	
	private static WXCommandCache wxCommandCache;
	
	
	private static PageIndexCache pageIndexCache;
	
	public static ArchCache getInstance(){
		if(instance == null)
			instance = new ArchCache(0);
		return instance;
	}
	
	

	//add other cache to here
	
	public ArchCache(int size)
	{
		sysConfigCache=new SysConfigCache(size);
		showTableConfigCache = new ShowTableConfigCache();
		
		roleCache = new RoleCache();
		
		gridMenuCache=new GridMenuCache();
		
		constantsCache = new ConstantsCache();
		
		metaDataCache = new MetaDataCache();
		
		dataImportCache = new DataImportCache();
		
		serviceInterfaceCache = new ServiceInterfaceCache();
		
		userConfigCache = new UserConfigCache();
		wxCommandCache=new WXCommandCache();
		
		pageIndexCache=new PageIndexCache();
	}

	public  UserConfigCache getUserConfigCache() {
		return userConfigCache;
	}



	public  SysConfigCache getSysConfigCache() {
		return sysConfigCache;
	}


	public  ShowTableConfigCache getShowTableConfigCache() {
		return showTableConfigCache;
	}
	
	
	public  RoleCache getRoleCache() {
		return roleCache;
	}



	public  GridMenuCache getGridMenuCache() {
		return gridMenuCache;
	}
	
	public ConstantsCache getConstantsCache(){
		return constantsCache;
	}



	public  MetaDataCache getMetaDataCache() {
		return metaDataCache;
	}



	public DataImportCache getDataImportCache() {
		return dataImportCache;
	}



	/*public void setDataImportCache(DataImportCache dataImportCache) {
		ArchCache.dataImportCache = dataImportCache;
	}*/



	public  ServiceInterfaceCache getServiceInterfaceCache() {
		return serviceInterfaceCache;
	}



	public  WXCommandCache getWxCommandCache() {
		return wxCommandCache;
	}


	
	public PageIndexCache getPageIndexCache(){
		return pageIndexCache;
	}
	
}
