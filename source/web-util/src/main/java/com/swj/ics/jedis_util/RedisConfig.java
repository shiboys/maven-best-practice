package com.swj.ics.jedis_util;

/**
 * Created by swj on 2016/11/27.
 */
public class RedisConfig {
    //可用链接实例的最大数目，默认是8
    public static int MAX_ACTIVE=1024;
    // 控制一个pool最多有多少个状态idle的jedis实例。默认也是8
    public  static int MAX_IDLE=200;

    //等待可用链接的最大时间，单位是毫秒，默认是-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
    public static int MAX_WAIT=10000;

    public static int TIMEOUT=10000;

    public static int RETRY_TIMES=5;
}
