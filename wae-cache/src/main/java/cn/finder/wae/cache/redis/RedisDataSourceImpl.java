package cn.finder.wae.cache.redis;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDataSourceImpl implements RedisDataSource{
	
	private static final Logger logger = Logger.getLogger(RedisDataSourceImpl.class);  
	  
    private ShardedJedisPool  shardedJedisPool;  
  
    public void setShardedJedisPool(ShardedJedisPool  shardedJedisPool){
    	this.shardedJedisPool=shardedJedisPool;
    }
    
    public ShardedJedis getRedisClient() {  
        try {  
            ShardedJedis shardJedis = shardedJedisPool.getResource();  
            return shardJedis;  
        } catch (Exception e) {  
        	logger.error("getRedisClent error", e);  
        }  
        return null;  
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
}
