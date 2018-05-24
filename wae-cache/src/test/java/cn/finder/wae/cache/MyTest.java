package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

public class MyTest {

	@Test
	public void test1(){
		
		
		Jedis jedis=new Jedis("127.0.0.1",6379);
		jedis.set("key1", "v1");
		jedis.setex("key2",5,"v2"); //5秒过期
		
		String v1=jedis.get("key1");
		String v2=jedis.get("key2");
		
		System.out.println("v1:"+v1+",v2="+v2);
		
		jedis.sadd("key_set", "v1","v2","哈哈");
		jedis.sadd("key_set", "v3","v2","哈哈2");
		
		Set<String> myset=jedis.smembers("key_set");
		
		jedis.srem("key_set", "v2");
		
		myset=jedis.smembers("key_set");
		
		jedis.scard("key_set");
		
		jedis.sismember("key_set", "v3");
		jedis.sismember("key_set", "noex");
		
		Map<String, String>  capital = new HashMap<String, String>();
		capital.put("shannxi", "xi'an");
		jedis.hmset("capital", capital);
		
		List<String> cities =jedis.hmget("capital", "shannxi");
		
		jedis.flushDB();
		
		
	}
	
	@Test
	public void test02(){
		
		
	}
}
