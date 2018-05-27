package com.swj.ics.redis_pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * Created by swj on 2017/11/30.
 */
public class PipelineTest {
    
    static final String HOST = "192.168.0.109";
    static final int PORT = 6379;
    static final String TEST_KEY = "testKey";
    static final int LOOP_COUNT = 100000; 
    
    static void usePipeline() {
        Jedis client = new Jedis(HOST,PORT);
        Pipeline pipeline = client.pipelined();
        for (int i = 0 ; i < LOOP_COUNT; i++) { //循环10万次
            pipeline.incr(TEST_KEY);
           // client.get(TEST_KEY);
        }
        pipeline.sync();
       // client.quit();
    }
    
    //不使用pipeline方式
    static void withoutPipeline() {
        Jedis client = new Jedis(HOST,PORT);
        for (int i = 0 ; i < LOOP_COUNT; i++) { //循环10万次
            client.incr(TEST_KEY);
            //client.get(TEST_KEY);
        }
        //client.quit();
    }
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        usePipeline();        
        long end = System.currentTimeMillis();
        System.out.println("with pipeline 10万次 incr 耗时："+(end - startTime));

        startTime = System.currentTimeMillis();
        withoutPipeline();
        end = System.currentTimeMillis();

        System.out.println("without pipeline 10万次 incr 耗时："+(end - startTime));
        
        /*
        * with pipeline 10万次 incr 耗时：447
without pipeline 10万次 incr 耗时：34281
        * */
    }
    
    
}
