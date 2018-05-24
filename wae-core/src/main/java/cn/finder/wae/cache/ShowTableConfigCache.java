package cn.finder.wae.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;

/***
 * 
 * 
 * @author wu hualong
 *
 */
public class ShowTableConfigCache{

	public final static String CACHE_KEY="cache_ShowTableConfig";
	
	private static Cache<String,Long,ShowTableConfig> cache;
	
	public void setCache(Cache<String,Long,ShowTableConfig> cache){
		ShowTableConfigCache.cache=cache;
	}
	
	public Cache<String,Long,ShowTableConfig> getCache(){
		return ShowTableConfigCache.cache;
	}
	
	/***
	 * 一般可能会通过名称来查找
	 * 
	 * 禁用， 因为 一般会返回多个 ShowTableConfig,但是可以用来返回 列配置，其他操作不应该调用此函数
	 * @param showTableName
	 * @return
	 */
	@Deprecated
	public ShowTableConfig  getShowTableConfig(String showTableName)
	{
		Set<Entry<Long, ShowTableConfig>> setData=get().entrySet();
		
		 Iterator<Entry<Long, ShowTableConfig>> ite= setData.iterator();
		 
		 while(ite.hasNext())
		 {
			 Entry<Long, ShowTableConfig> entry = ite.next();
			 
			 ShowTableConfig tableConfig = entry.getValue();
			 if(showTableName.equalsIgnoreCase(tableConfig.getShowTableName()))
			 {
				 return tableConfig;
			 }
					 
		 }
		
		return null;
		
		
		
	}
	
	
	/***
	 * 根据表明  返回列配置
	 * @param showTableName
	 * @return
	 */
	public List<ShowDataConfig>  getShowDataConfigs(String showTableName)
	{
		
		
		/*Set<Entry<Long, ShowTableConfig>> setData=baseDatas.entrySet();
		
		 Iterator<Entry<Long, ShowTableConfig>> ite= setData.iterator();
		 
		 while(ite.hasNext())
		 {
			 Entry<Long, ShowTableConfig> entry = ite.next();
			 
			 ShowTableConfig tableConfig = entry.getValue();
			 if(showTableName.equalsIgnoreCase(tableConfig.getShowTableName()))
			 {
				 return tableConfig.getShowDataConfigs();
			 }
					 
		 }*/
		
		
		
		
		return null;
	}
	
	
	/****
	 * 根据表名和别名返回 ShowDataConfig
	 * @param tableName
	 * @param fieldNameAlias
	 * @return
	 */
	public ShowDataConfig getShowDataConfig(String tableName,String fieldNameAlias){
		
		List<ShowDataConfig> showDataConfigs =getShowDataConfigs(tableName);
		for(ShowDataConfig sdc:showDataConfigs){
			if(sdc.getFieldNameAlias().equalsIgnoreCase(fieldNameAlias)){
				return sdc;
			}
		}
		return null;
	}
	
	
	/***
	 * 
	 * @param tableName  表名称
	 * @param fieldName  字段名称
	 * @return
	 */
	public ShowDataConfig getShowDataConfigByFieldName(String tableName,String fieldName){
		
		List<ShowDataConfig> showDataConfigs =getShowDataConfigs(tableName);
		for(ShowDataConfig sdc:showDataConfigs){
			if(sdc.getFieldName().equalsIgnoreCase(fieldName)){
				return sdc;
			}
		}
		return null;
	}
	
	
	/***
	 * 获取主键对应的ShowDataConfig记录 默认ID
	 * @param showtableConfigId
	 * @return
	 */
	@Deprecated
	public ShowDataConfig  getShowDataConfig(long showtableConfigId)
	{
		return getShowDataConfig(showtableConfigId,"id");
	}
	
	/***
	 * 获取主键对应的ShowDataConfig记录 配置is_primarkey=1
	 * @param showtableConfigId
	 * @return
	 */
	public ShowDataConfig  getPKShowDataConfig(long showtableConfigId)
	{
		ShowTableConfig showtableConfig = get(showtableConfigId);
		
		List<ShowDataConfig> showDataConfigs=showtableConfig.getShowDataConfigs();
		 
		for(ShowDataConfig showDataConfig:showDataConfigs)
		{
			if(showDataConfig.getIsPrimaryKey()==1)
			{
				return showDataConfig;
			}
		}
		
		return getShowDataConfig(showtableConfigId);
	}
	
	
	
	/***
	 * 获取联合主键列配置
	 * @param showtableConfigId
	 * @return
	 */
	public List<ShowDataConfig>  getPKShowDataConfigs(long showtableConfigId)
	{
		List<ShowDataConfig> pks=new ArrayList<ShowDataConfig>();
		
		ShowTableConfig showtableConfig = get(showtableConfigId);
		
		List<ShowDataConfig> showDataConfigs=showtableConfig.getShowDataConfigs();
		 
		for(ShowDataConfig showDataConfig:showDataConfigs)
		{
			if(showDataConfig.getIsPrimaryKey()==1)
			{
				pks.add(showDataConfig);
			}
		}
		
		return pks;
	}
	
	
	
	
	/***
	 * 获取主键对应的ShowDataConfig记录 配置is_primarkey=1
	 * @param showtableConfigId
	 * @return
	 */
	public ShowDataConfig  getShowDataConfigById(long showDataConfigId)
	{
		/*Set<Entry<Long, ShowTableConfig>> setMap =baseDatas.entrySet();
		
		Iterator<Entry<Long, ShowTableConfig>> ite = setMap.iterator();
		
		while(ite.hasNext()){
			
			Entry<Long, ShowTableConfig> entry = ite.next();
			
			ShowTableConfig stc = entry.getValue();
			
			List<ShowDataConfig> sdcList = stc.getShowDataConfigs();
			
			for(ShowDataConfig sdc:sdcList){
				if(showDataConfigId == sdc.getId())
					return sdc;
			}
		}
		return null;*/
		
		
		ShowTableConfig stc = get(showDataConfigId);
		List<ShowDataConfig> sdcList = stc.getShowDataConfigs();
		
		for(ShowDataConfig sdc:sdcList){
			if(showDataConfigId == sdc.getId())
				return sdc;
		}
		return null;
	}
	
	
	/***
	 * 返回特定字段的  ShowDataConfig记录
	 * @param showtableConfigId
	 * @param fieldName
	 * @return
	 */
	public ShowDataConfig  getShowDataConfig(long showtableConfigId,String fieldName)
	{
		ShowTableConfig showtableConfig = get(showtableConfigId);
		
		List<ShowDataConfig> showDataConfigs=showtableConfig.getShowDataConfigs();
		 
		for(ShowDataConfig showDataConfig:showDataConfigs)
		{
			if(showDataConfig.getFieldName().equalsIgnoreCase(fieldName))
			{
				return showDataConfig;
			}
		}
		
		return null;
	}
	
	
	/***
	 * 根据表配置和别名获取 ShowDataConfig
	 * @param showtableConfigId
	 * @param fieldNameAlian
	 * @return
	 */
	public ShowDataConfig  getShowDataConfigForFieldNameAlian(long showtableConfigId,String fieldNameAlian)
	{
		ShowTableConfig showtableConfig = get(showtableConfigId);
		
		List<ShowDataConfig> showDataConfigs=showtableConfig.getShowDataConfigs();
		 
		for(ShowDataConfig showDataConfig:showDataConfigs)
		{
			if(showDataConfig.getFieldNameAlias().equalsIgnoreCase(fieldNameAlian))
			{
				return showDataConfig;
			}
		}
		
		return null;
	}
	
	
	/***
	 * 获取通用搜索的 字段列表
	 * @param showtableConfigId
	 * @return
	 */
	public List<ShowDataConfig> getSearchShowDataConfigs(long showtableConfigId)
	{
		List<ShowDataConfig> sdcs=new ArrayList<ShowDataConfig>();
		ShowTableConfig showtableConfig = get(showtableConfigId);
		
		List<ShowDataConfig> showDataConfigs=showtableConfig.getShowDataConfigs();
		 
		for(ShowDataConfig showDataConfig:showDataConfigs)
		{
			if(showDataConfig.getIsShowSearch()==1)
			{
				sdcs.add(showDataConfig);
			}
		}
		
		return sdcs;
	}
	
	
	/***
	 * 获取 表配置 列配置    逗号隔开
	 * @param showtableConfigId
	 * @return
	 */
	public String getFieldsString(long showtableConfigId){
		  
		ShowTableConfig showtableConfig = get(showtableConfigId);
		
		List<ShowDataConfig> showDataConfigs=showtableConfig.getShowDataConfigs();
	    List<String> filedNames=new ArrayList<String>();
	   
	    
	    for(ShowDataConfig df:showDataConfigs)
	    {
	    	filedNames.add(df.getFieldName()+" '"+df.getFieldNameAlias()+"'");
	    }
	    
	    return StringUtils.join(filedNames, ',');
	    
	    
	}
	
	public Map<Long, ShowTableConfig> getBaseDatas() {
		return get();
	}

	public boolean add(Long key, ShowTableConfig value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<Long,ShowTableConfig> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(Long key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(Long key, ShowTableConfig newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(Long key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}



	
	



	

	public HashMap<Long, ShowTableConfig> get() {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY);
	}

	public ShowTableConfig get(Long key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}



	public ShowTableConfig getList(Long key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY, key);
	}



	public List<ShowTableConfig> getList(long startIndex, long endIndex) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,startIndex, endIndex);
	}



	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
	
	
}
