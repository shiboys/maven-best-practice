package com.swj.ics.jedis_util;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swj on 2016/11/27.
 */
public class JedisUtil {
    private Logger logger= Logger.getLogger(this.getClass().getName());
    private JedisUtil()
    {

    }
    private static class JedisUtilFactory
    {
         static  final JedisUtil instance=new JedisUtil();
    }
    public static JedisUtil getInstance()
    {
        return JedisUtilFactory.instance;
    }

    private Map<String,JedisPool> maps=new HashMap<String, JedisPool>();

    private JedisPool getJedisPool(String host,int port)
    {
        JedisPool pool=null;
        String key=host+":"+port;
        pool=maps.get(key);
        if(pool==null)
        {
            JedisPoolConfig config=new JedisPoolConfig();
            config.setMaxTotal(RedisConfig.MAX_ACTIVE);
            config.setMaxIdle(RedisConfig.MAX_IDLE);
            config.setMinIdle(8);
            config.setMaxWaitMillis(RedisConfig.MAX_WAIT);

            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            config.setTestWhileIdle(true);
            pool=new JedisPool(config,host,port,RedisConfig.TIMEOUT);

            maps.put(key,pool);
        }
        return pool;
    }
    public Jedis getJedis(String ip,int port)
    {
        Jedis jedis=null;
        int count=0;
        do {
            try {
                JedisPool pool=getJedisPool(ip,port);
                jedis=pool.getResource();
            }
            catch (Exception ex)
            {
                logger.error("get redis master1 failed",ex);
                getJedisPool(ip,port).returnBrokenResource(jedis);
            }
            count++;
        }while(jedis==null && count< RedisConfig.RETRY_TIMES);
            return jedis;
    }

    public void closeJedis(String ip,int port,Jedis jedis)
    {
        if(jedis!=null)
        {
            getJedisPool(ip,port).returnResource(jedis);
        }
    }

}
