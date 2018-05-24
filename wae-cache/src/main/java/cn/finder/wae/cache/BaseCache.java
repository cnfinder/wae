package cn.finder.wae.cache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 
 * @author finder
 *
 * @param <C>
 * @param <K>
 * @param <V>  值使用的是 HM  hash类型
 */
public abstract  class BaseCache<C,K,V> implements Cache<C,K,V>{

	
	
	public String getString(C cache_key,K key) {
		V v=get(cache_key,key);
		if(v!=null){
			return v.toString();
		}
		return null;
	}

	public Integer getInt(C cache_key,K key) {
		V v=get(cache_key,key);
		if(v!=null){
			return Integer.valueOf(v.toString());
		}
		return null;
	}

	public Float getFloat(C cache_key,K key) {
		V v=get(cache_key,key);
		if(v!=null){
			return Float.valueOf(v.toString());
		}
		return null;
	}

	public Date getDate(C cache_key,K key) {
		V v=get(cache_key,key);
		if(v!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				Date d=sdf.parse(v.toString());
				return d;
			} catch (ParseException e) {
				return null;
			}
			
		}
		return null;
	}

	public Long getLong(C cache_key,K key) {
		V v=get(cache_key,key);
		if(v!=null){
			return Long.valueOf(v.toString());
		}
		return null;
	}

}
