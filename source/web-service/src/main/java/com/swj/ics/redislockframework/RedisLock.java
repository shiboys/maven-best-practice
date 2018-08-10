package com.swj.ics.redislockframework;

import java.util.Random;

/**
 */
public class RedisLock {
    //纳秒和毫秒之间的转换。1秒 =1000毫秒 。1毫秒= 1000*1000L
    public static  final long MILLI_NANO_TIME = 1000*1000L;
    private static final String LOCKED_VALUE = "TRUE";
    public static final Random RANDOM = new Random();
    private String key;
    //封装redis操作的客户端。
    private RedisClient redisClient;
    
    private boolean lock = true;
    
    public RedisLock(String purpose,String key) {
        this.key = purpose + "_" + key + "_lock";
        this.redisClient = RedisFactory.getDefaultClient();
    }
    
    public RedisLock(String purpose,String key,RedisClient client) {
        this.key = purpose + "_" + key + "_lock";
        this.redisClient = client;
    }

    /**
     * 加锁
     * 使用方式为:
     * lock();
     * try {
     *     executeMethod()
     * } finally {
     *     unlock();
     * }
     * @param timeout timeout的时间范围内(单位是毫秒)来轮询锁是否可用的时间
     * @param expire 设置锁的过期时间。单位是秒
     * @return 成功 or 失败
     */
    public boolean lock (long timeout,int expire) {
        //4,999,610,094
        long nanoTime = System.nanoTime();
        //转换成 nanoSecond为单位的
        timeout = timeout * MILLI_NANO_TIME;

        try {
            //在timeout的时间范围之内不断轮询锁
            while (System.nanoTime() - nanoTime < timeout) {
                long lockedResult = this.redisClient.setnx(this.key,LOCKED_VALUE);
                if (lockedResult == 1) {
                    //设置锁的过期时间，是为了在没有释放锁的情况下，到了过期时间，锁自动消失。
                    //不会造成永久阻塞
                    this.redisClient.expire(this.key,expire);
                    this.lock = true;
                    return this.lock;
                }
                System.out.println("出现等待锁");
                //进行短暂休眠，避免可能出现的活锁
                // while循环是一个阻塞式的加锁方法， 以一定的时间间隔来轮询，否则Redis会吃不肖。
                Thread.sleep(3,RANDOM.nextInt(100));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("reids lock exception",e);
        }
        return false;
    }
    
    public void unLock() {
        try {
            if (this.lock) {
                this.redisClient.delKey(this.key);//直接删除被锁的key,这样锁也就消失了
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
