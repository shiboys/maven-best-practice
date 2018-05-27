package com.swj.ics.redislockframework;

import redis.clients.jedis.JedisPool;

/**
 * Created by swj on 2017/11/26.
 */
public class RedisFactory {
    
    public static RedisClient getDefaultClient() {
        JedisPool jedisPool = new JedisPool("192.168.0.109",6379);
        return new RedisClient(jedisPool);
    }
}
