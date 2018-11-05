package com.swj.ics.redisHighLevel;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import redis.clients.jedis.Jedis;

/**
 * Created by swj on 2018/9/10.
 */
public class RedisQueue<T> {

    static final String HOST = "192.168.0.100";
    static final int PORT = 6379;

    static class DelayItem<T> {
        public String id;
        public T msg;
    }

    private String delayKey;
    private Jedis jedis;
    private final Type taskType = (new TypeReference<DelayItem<T>>() {

    }).getType();

    public RedisQueue(Jedis jedis, String delayKey) {
        this.jedis = jedis;
        this.delayKey = delayKey;
    }

    public void putToDelayQueue(T data) {
        //String serializedT = JSON.toJSONString(data);
        String id = UUID.randomUUID().toString();
        DelayItem item = new DelayItem();
        item.id = id;
        item.msg = data;
        String json = JSON.toJSONString(item);
        jedis.zadd(delayKey, System.currentTimeMillis() + 5000, json);
    }

    public void loop() {
        while (!Thread.interrupted()) {
            Set<String> valueSet = jedis.zrangeByScore(delayKey, 0, System.currentTimeMillis(), 0, 1);
            if (valueSet.isEmpty()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                continue;
            }
            String zValue = valueSet.iterator().next();
            if (jedis.zrem(delayKey, zValue) > 0) { //抢到机会
                DelayItem<T> delayItem = JSON.parseObject(zValue, taskType);
                handleMsg(delayItem.msg);
            }
        }

    }

    public <T> void handleMsg(T msg) {
        System.out.println(msg.toString());
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis(HOST, PORT);
        String delayKey = "delayKey";
        RedisQueue queue = new RedisQueue(jedis,delayKey);
        
        Thread producer = new Thread(() -> {
            for (int i=0;i<10;i++) {
                queue.putToDelayQueue("delay msg " +i);
            }
        },"producer");
        
        Thread consumer = new Thread(()->{
            queue.loop();
        },"consumer");
        
        producer.start();
        consumer.start();

        try {
            producer.join();
            Thread.sleep(10000);
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
