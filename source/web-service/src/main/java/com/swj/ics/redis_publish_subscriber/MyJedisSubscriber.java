package com.swj.ics.redis_publish_subscriber;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by swj on 2017/11/27.
 */
public class MyJedisSubscriber extends JedisPubSub {
    //http://blog.csdn.net/lihao21/article/details/48370687

    public  MyJedisSubscriber() {
        
    }
    
    @Override
    public void onMessage(String channel, String message) {
        String msg = String.format("received msg from redis server:channel :%s,"+
        "message:%s",channel,message);
        System.out.println(msg);
    }
    

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        String msg = String.format("subscribe redis channel success !"+
        "channel:%s,subscribedChannels count:%d",channel,subscribedChannels);
        System.out.println(msg);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel,channel" +
                ":%s,subscribedChannels count:%d",channel,subscribedChannels));
    }
}
