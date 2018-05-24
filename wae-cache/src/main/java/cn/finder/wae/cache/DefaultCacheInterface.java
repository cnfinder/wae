package cn.finder.wae.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DefaultCacheInterface<K,V> implements CacheInterface<K, V>{

	public boolean add(K key, V value) {
		return false;
	}

	public boolean add(HashMap<K, V> value) {
		return false;
	}

	public V get(K key) {
		return null;
	}

	public HashMap<K, V> get() {
		return null;
	}

	public List<V> get(K... key) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<V> get(long startIndex, long endIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean replace(K key, V newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean clear() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getString(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getInt(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Float getFloat(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getLong(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean add(K key, V value, long expire) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean add(HashMap<K, V> value, long expire) {
		// TODO Auto-generated method stub
		return false;
	}

}
