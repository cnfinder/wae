package cn.finder.wae.cache.redis;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClientTemplate {
	private static final Logger log = Logger.getLogger(RedisClientTemplate.class);  
	  
    
    private ShardedJedisPool  shardedJedisPool;  
  
    public void setShardedJedisPool(ShardedJedisPool  shardedJedisPool){
    	this.shardedJedisPool=shardedJedisPool;
    }
      
    public ShardedJedis getRedisClient() {  
        try {  
            ShardedJedis shardJedis = shardedJedisPool.getResource();  
            return shardJedis;  
        } catch (Exception e) {  
            log.error("getRedisClent error", e);  
        }  
        return null;  
    }  
      
    /**  
     * 获取单个值  
     *   
     * @param key  
     * @return  
     */  
    public String get(String key) {  
        String result = null;  
        ShardedJedis shardedJedis = shardedJedisPool.getResource();  
        if (shardedJedis == null) {  
            return result;  
        }  
  
        boolean broken = false;  
        try {  
            result = shardedJedis.get(key);  
  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
      
      
    public String get(String key,ShardedJedis shardedJedis,boolean isReturn) {  
        String result = null;  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.get(key);  
  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            if(isReturn){  
                returnResource(shardedJedis, broken);  
            }  
        }  
        return result;  
    }  
      
    public String set(String key, Object value,ShardedJedis shardedJedis,boolean isReturn) {  
        String result = null;  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.set(key.getBytes(), SerializeUtils.serialize(value));  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            if(isReturn){  
                returnResource(shardedJedis, broken);  
            }  
        }  
        return result;  
    }  
      
    /**  
     * 设置单个值  
     *   
     * @param key  
     * @param value  
     * @return  
     */  
    public String set(String key, Object value) {  
        String result = null;  
  
        ShardedJedis shardedJedis = shardedJedisPool.getResource();  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.set(key.getBytes(), SerializeUtils.serialize(value));  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
      
      
      
    public String setsx(String key, String value,Integer seconds) {  
        String result = null;  
  
        ShardedJedis shardedJedis = shardedJedisPool.getResource();  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.setex(key,seconds, value);  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
      
    public String setsx(String key, String value,Integer seconds,ShardedJedis shardedJedis,boolean isReturn) {  
        String result = null;  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.setex(key,seconds, value);  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            if(isReturn){  
                returnResource(shardedJedis, broken);  
            }  
        }  
        return result;  
    }  
  
      
      
  
    public void returnResource(ShardedJedis shardedJedis) {  
        shardedJedisPool.returnResource(shardedJedis);  
    }  
  
    public void returnResource(ShardedJedis shardedJedis, boolean broken) {  
        if (broken) {  
            shardedJedisPool.returnBrokenResource(shardedJedis);  
        } else {  
            shardedJedisPool.returnResource(shardedJedis);  
        }  
    }  
      
      
      
    public void disconnect() {  
        ShardedJedis shardedJedis = shardedJedisPool.getResource();  
        shardedJedis.disconnect();  
    }  
  
      
  
    public Boolean exists(String key) {  
        Boolean result = false;  
        ShardedJedis shardedJedis = getRedisClient();  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.exists(key);  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
  
    public String type(String key) {  
        String result = null;  
        ShardedJedis shardedJedis = getRedisClient();  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.type(key);  
  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
  
    /**  
     * 在某段时间后失效  
     *   
     * @param key  
     * @param unixTime  
     * @return  
     */  
    public Long expire(String key, int seconds) {  
        Long result = null;  
        ShardedJedis shardedJedis = getRedisClient();  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.expire(key, seconds);  
  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
  
    /**  
     * 在某个时间点失效  
     *   
     * @param key  
     * @param unixTime  
     * @return  
     */  
    public Long expireAt(String key, long unixTime) {  
        Long result = null;  
        ShardedJedis shardedJedis = getRedisClient();  
        if (shardedJedis == null) {  
            return result;  
        } 
        boolean broken = false;  
        try {  
            result = shardedJedis.expireAt(key, unixTime);  
  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
  
    public Long del(String key) {  
        Long result = null;  
        ShardedJedis shardedJedis = getRedisClient();  
        if (shardedJedis == null) {  
            return result;  
        }  
        boolean broken = false;  
        try {  
            result = shardedJedis.del(key);  
  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
            broken = true;  
        } finally {  
            returnResource(shardedJedis, broken);  
        }  
        return result;  
    }  
   
}
