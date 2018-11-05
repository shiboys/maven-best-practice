package com.swj.ics.redislockframework;

import java.util.Random;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * author shiweijie
 * date 2018/8/7 下午9:03
 */
@Service
public class RedisExpireLock  {

    private static final int DEFAULT_LOOP_MILLIS = 100;

    private volatile boolean locked = false;

    private static final Random random = new Random();

    private String key;
    //封装redis操作的客户端。
    private RedisClient redisClient;

    public RedisExpireLock(String purpose,String key) {
        this.key = purpose + "_" + key + "_lock";
        this.redisClient = RedisFactory.getDefaultClient();
    }

    public RedisExpireLock(String purpose,String key,RedisClient client) {
        this.key = purpose + "_" + key + "_lock";
        this.redisClient = client;
    }


    public boolean lock( long timeout, int expire) {
        while (timeout >= 0) {
            long currentTimeMillis = System.currentTimeMillis();
            long expireTimeMillis = currentTimeMillis + expire + 1;
            String expireStr = String.valueOf(expireTimeMillis);

            if (redisClient.setnx(key, expireStr) == 1) {
                this.locked = true;
                return true;
            }

            String currentRedisTime = redisClient.getByKey(key).toString();
            if (currentRedisTime != null && Long.parseLong(currentRedisTime) < System.currentTimeMillis()) {
                //redis的锁 已过期
                String oldRedisTime = redisClient.getSetT(key, expireStr);
                if (oldRedisTime != null && oldRedisTime.equalsIgnoreCase(currentRedisTime)) {
                    /**
                     * 因为是分布式的环境下，可以在前一个锁失效的时候，有两个进程进入到锁超时的判断。如：

                     C0超时了，还持有锁,C1/C2同时请求进入了方法里面

                     C1/C2获取到了C0的超时时间

                     C1使用getSet方法

                     C2也执行了getSet方法

                     假如不加 oldValueStr.equals(currentValueStr) 的判断，将会C1/C2都将获得锁，加了之后，能保证C1和C2只能一个能获得锁，一个只能继续等待。
                     这里可能导致超时时间不是其原本的超时时间，C1的超时时间可能被C2覆盖了，但是他们相差的毫秒及其小，这里忽略了
                     */
                    this.locked = true;
                    return true;
                }
            }
            int waitTime = DEFAULT_LOOP_MILLIS + random.nextInt(10);
            timeout -= waitTime;
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return false;
    }

    public void unLock() {
        try {
            if (locked) {
                redisClient.delKey(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
