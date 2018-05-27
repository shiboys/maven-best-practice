package com.swj.ics.redis_publish_subscriber;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by swj on 2017/11/28.
 */
public class MyRedisPublisher {
    private JedisPool jedisPool = null;
    public MyRedisPublisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    public void start() {
        BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(System.in));
        Jedis client = null;
        try {
            client = jedisPool.getResource();
            System.out.println("请输入需要发布的消息! quit 退出");
            String msg = bufferedInputStream.readLine();
            while (!"quit".equals(msg)) {
                client.publish(SubscribeThread.CHANNEL,msg);
                System.out.println("请再次输入需要发布的消息! quit 退出");
                msg = bufferedInputStream.readLine();
            }
            
        } catch (IOException e) {
            System.out.println("jedis publish exception :" + e);
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(client);
        }
    }
}
