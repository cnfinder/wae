package cn.finder.wae.cache.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import cn.finder.wae.cache.BaseCache;


/***
 * redis cache 实现
 * @author finder
 *
 */
public class RedisCache extends BaseCache<Serializable,Serializable, Serializable>
{
	
	
	
	private static RedisTemplate<Serializable,Serializable> redisTemplate=null;
	
	
	public void setRedisTemplate(RedisTemplate<Serializable,Serializable> redisTemplate){
		RedisCache.redisTemplate=redisTemplate;
	}
	
	public static RedisTemplate<Serializable,Serializable> getRedisTemplate(){
		return RedisCache.redisTemplate;
	}

	
	/** 
     * 获取键的 序列化机制 RedisSerializer 
     * 这里为了 扩展性好 都用jdk 序列化机制
     * <br>------------------------------<br> 
     */  
    protected  JdkSerializationRedisSerializer getKeyRedisSerializer() {  
        return (JdkSerializationRedisSerializer)redisTemplate.getValueSerializer();
    }  
    
    /**
     * 获取值的序列化机制
     * @return
     */
    protected  JdkSerializationRedisSerializer getValueRedisSerializer() {  
        return (JdkSerializationRedisSerializer)redisTemplate.getValueSerializer();
    }  
    
    public  boolean add(final Serializable cache_key,Serializable key,Serializable value) {
    	return add(cache_key,key,value,0);
	}
    
    public  boolean add(final Serializable cache_key,Serializable key,Serializable value,long expire){
    	HashMap<Serializable,Serializable> values=new HashMap<Serializable, Serializable>();
    	values.put(key, value);
		return add(cache_key,values,expire);
    }
    
	public  boolean add(final Serializable cache_key,final HashMap<Serializable,Serializable> value) {
		return add(cache_key,value,0);
	}
	
	public  boolean add(final Serializable cache_key,final HashMap<Serializable,Serializable> value,final long expire) {
		
		boolean result=redisTemplate.execute(new RedisCallback<Boolean>(){

			public Boolean doInRedis(RedisConnection connection){
				
				
				
				byte[] cache_key_bytes=getKeyRedisSerializer().serialize(cache_key);
				
				BoundHashOperations<Serializable, byte[], byte[]> bytes_value = redisTemplate.boundHashOps(cache_key_bytes);
				
				Set<Entry<Serializable,Serializable>> s= value.entrySet();
				Iterator<Entry<Serializable,Serializable>> iter= s.iterator();
				
				while(iter.hasNext()){
					Entry<Serializable,Serializable> entry= iter.next();
					Serializable k=entry.getKey();
					Serializable v=entry.getValue();
					
					byte[] k_bytes= getValueRedisSerializer().serialize(k);
					byte[] v_bytes= getValueRedisSerializer().serialize(v);
					
					bytes_value.put(k_bytes, v_bytes);
					
							
				}
				connection.hMSet(cache_key_bytes, bytes_value.entries());
				if(expire>0){
					connection.expire(cache_key_bytes, expire);
				}
                return true; 
			}
			
		});
		return result;
		
	}
	
	
	public List<Serializable> get(final Serializable cache_key,final Serializable... key) {
		
		List<Serializable> ret=redisTemplate.execute(new RedisCallback<List<Serializable>>(){

			public List<Serializable> doInRedis(RedisConnection connection){
				
				byte[] cache_key_bytes=getKeyRedisSerializer().serialize(cache_key);
				//byte[] value_bytes=connection.get(key_bytes);
				
				byte[][] key_bytes_arr=new byte[key.length][];
				for(int i=0;i<key.length;i++){
					//key_bytes_arr.add(getValueRedisSerializer().serialize(key[i]));
					
					key_bytes_arr[i]=getValueRedisSerializer().serialize(key[i]);
				}
				
				List<byte[]> value_bytes=connection.hMGet(cache_key_bytes,key_bytes_arr);
				
				List<Serializable> value_ser=new ArrayList<Serializable>();
				for(byte[] item_byte:value_bytes){
					value_ser.add((Serializable)getValueRedisSerializer().deserialize(item_byte));
				}
				
				return value_ser;
				
			}
			
		});
		return ret;
	}
	
	public Serializable get(final Serializable cache_key,final Serializable key) {
		Serializable ret=redisTemplate.execute(new RedisCallback<Serializable>(){
			public Serializable doInRedis(RedisConnection connection){
				
				byte[] cache_key_bytes=getKeyRedisSerializer().serialize(cache_key);
				byte[] key_bytes=getValueRedisSerializer().serialize(key);
				byte[] value_bytes=connection.hGet(cache_key_bytes, key_bytes);
				
				Object o=getValueRedisSerializer().deserialize(value_bytes);
				return (Serializable)o;
			}
			
		});
		return ret;
	}
	
	
	public HashMap<Serializable,Serializable> get(final Serializable cache_key) {
		
		
		return redisTemplate.execute(new RedisCallback<HashMap<Serializable,Serializable>>(){

			public HashMap<Serializable,Serializable> doInRedis(RedisConnection connection){
				
				HashMap<Serializable,Serializable> retData=new HashMap<Serializable, Serializable>();
				
				byte[] cache_key_bytes=getKeyRedisSerializer().serialize(cache_key);
				
				Map<byte[],byte[]> map=connection.hGetAll(cache_key_bytes);
				if(map!=null){
					Iterator<Entry<byte[],byte[]>> itr= map.entrySet().iterator();
					
					while(itr.hasNext()){
						Entry<byte[],byte[]> entry=itr.next();
						Object key_data=getKeyRedisSerializer().deserialize(entry.getKey());
						Object value_data=getValueRedisSerializer().deserialize(entry.getValue());
						
						retData.put((Serializable)key_data, (Serializable)value_data);
					}
				}
				
				
				
				return retData;
				
			}
			
		});
	}
	

	public List<Serializable> get(Serializable cache_key, long startIndex,
			long endIndex) {

		return null;
	}


	
	

	public boolean remove(final Serializable cache_key,final Serializable key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {  
	        public Boolean doInRedis(RedisConnection connection) {  
	            byte[] cache_key_bytes = getKeyRedisSerializer().serialize(cache_key); 
	            byte[] key_bytes = getValueRedisSerializer().serialize(cache_key); 
	            connection.hDel(cache_key_bytes, key_bytes);
	            return true;  
	        }  
	    }); 
		
	}
	public boolean replace(final Serializable cache_key,final Serializable key,final Serializable value) {
		
		boolean result=redisTemplate.execute(new RedisCallback<Boolean>(){

			public Boolean doInRedis(RedisConnection connection){
				
				byte[] cache_key_bytes=getKeyRedisSerializer().serialize(cache_key);
				byte[] key_bytes=getValueRedisSerializer().serialize(key);
				byte[] value_bytes= getValueRedisSerializer().serialize(value);
				connection.hSet(cache_key_bytes,key_bytes, value_bytes);
                return true; 
			}
			
		});
		
		return result;
	}
	
	
	public boolean contains(final Serializable cache_key) {
		boolean ret=redisTemplate.execute(new RedisCallback<Boolean>(){

			public Boolean doInRedis(RedisConnection connection){
				
				byte[] key_bytes=getKeyRedisSerializer().serialize(cache_key);
				if(connection.exists(key_bytes)){
					return true;
				}
				
                return true; 
			}
			
		});
		return ret;
	}
	public boolean contains(final Serializable cache_key,final Serializable key) {
		boolean ret=redisTemplate.execute(new RedisCallback<Boolean>(){

			public Boolean doInRedis(RedisConnection connection){
				
				byte[] cache_key_bytes=getKeyRedisSerializer().serialize(cache_key);
				byte[] key_bytes=getValueRedisSerializer().serialize(key);
				if(connection.hExists(cache_key_bytes,key_bytes)){
					return true;
				}else{
					return false;
				}
			}
			
		});
		return ret;
	}
	
	
	public boolean clear(){
		return clear("*");
	}
	public boolean clear(final Serializable cache_key) {
		
		
		boolean ret=redisTemplate.execute(new RedisCallback<Boolean>(){

			public Boolean doInRedis(RedisConnection connection){
				
				//如果删除所有  传递 *
				Set<Serializable> keys = redisTemplate.keys(cache_key);
				
				if(keys!=null){
					
					 for(Serializable key:keys){
						 byte[] key_bytes=getKeyRedisSerializer().serialize(key);
						 connection.del(key_bytes);
						 
					 }
				}
					
                return true; 
			}
			
		});
		return ret;
		
	}



	


	
}
