package cn.finder.wae.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface CacheInterface<K,V> {

	/***
	 * 添加到缓存
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(K key,V value);
	
	
	
	/***
	 * 添加 一组值
	 * @param cache_key
	 * @param value
	 * @return
	 */
	public  boolean add(HashMap<K,V> value);
	
	
	/***
	 * 添加到缓存
	 * @param cache_key
	 * @param key
	 * @param value
	 * @param expire 过期时间 单位 秒
	 * @return 
	 */
	public boolean add(K key,V value,long expire);
	
	/***
	 * 添加 一组值
	 * @param cache_key
	 * @param value
	 * @param expire 过期时间 单位 秒
	 * @return
	 */
	public  boolean add(HashMap<K,V> value,long expire);
	
	/***
	 * 获取 某域的一个值
	 * @param key
	 * @return
	 */
	public V get(K key);
	
	
	/***
	 * 获取 一个域的所有的值
	 * @param cache_key
	 * @return
	 */
	public HashMap<K,V> get();
	
	
	
	/***
	 * 获取 一个域的 多个值
	 * @param key
	 * @param name
	 * @return
	 */
	public List<V> get(K... key);
	
	
	/***
	 * 获取某个域中  范围值 分页获取使用
	 * @param key
	 * @param startIndex 开始的索引
	 * @param endIndex  结束的索引
	 * @return
	 */
	public List<V> get(long startIndex,long endIndex);
	
	
	
	/***
	 * 移除某个域中的一个值
	 * @param key
	 * @return
	 */
	public boolean remove(K key);
	
	
	/***
	 * 替换某个域中的 一个值
	 * @param key
	 * @param newValue
	 * @return
	 */
	public boolean replace(K key,V newValue);
	

	
	
	/***
	 * 是否包含某个域中的 一个值
	 * @param cache_key
	 * @param key
	 * @return
	 */
	public boolean contains(K key);
	
	
	
	
	
	/***
	 * 清空所有的键值
	 */
	public boolean clear();
	
	
	
	
	public String getString(K key);
	public Integer getInt(K key);
	public Float getFloat(K key);
	public Date getDate(K key);
	public Long getLong(K key);
}
