package com.swj.ics.redis.redis_publish_subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by swj on 2017/11/27.
 */
public class RedisPubSubTest {

    static final String host = "192.168.0.109";
    static final int port = 6379;
    
    public static void main(String[] args) {

        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),host,port);
        //SocketPubSubDemo1();
        System.out.println(String.format("redis pool is starting... redis ip %s,redis port %d",host,port));
        SubscribeThread subscribeThread = new SubscribeThread(jedisPool);
        subscribeThread.start();

        MyRedisPublisher publisher = new MyRedisPublisher(jedisPool);
        publisher.start();
    }

    private static void SocketPubSubDemo1() {
        try {
          
            Socket socket = new Socket(host,port);
            InputStream inputStream = socket.getInputStream();
            OutputStream writer = socket.getOutputStream();
            //发送订阅命令
            Scanner sc  = new Scanner( System.in);
            System.out.println("请输入redis订阅命令");
            String cmd = sc.nextLine(); //next方法获取一个以space，tab或enter结束的字符串。
            //nextLine() 获取输入的一行字符串
            Jedis client = new Jedis(host,port);
            client.set("fuck","OK1");

            //JedisPubSub pubSub =  
            
            writer.write(cmd.getBytes());
            writer.flush();
            byte[] buffer = new byte[1024];
            
            while (true) {                
                int readCount = inputStream.read(buffer);
                System.out.write(buffer,0,readCount);
                
                System.out.println("---------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
