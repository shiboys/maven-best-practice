package com.swj.ics.redis_publish_subscriber;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by swj on 2017/11/28.
 */
public class SubscribeThread extends Thread {
    
    private final JedisPool jedisPool;
    private final MyJedisSubscriber subscriber = new MyJedisSubscriber();
    
    public static final String CHANNEL = "jedisChannel";
    public SubscribeThread(JedisPool jedisPool) {
        super("SubscribeThread");
        this.jedisPool = jedisPool;
    }
    
    @Override
    public void run() {
        System.out.println(String.format("subscribe redis,channel is %s" +
                ",thread will be blocked",CHANNEL));
        Jedis client = null;
        try {
            client = jedisPool.getResource();
            client.subscribe(subscriber,CHANNEL);
        } catch (Exception e) {
            System.out.println("subscribe channel error :" +e.getMessage());
        }finally {
            jedisPool.returnResource(client);
        }
    }
}
