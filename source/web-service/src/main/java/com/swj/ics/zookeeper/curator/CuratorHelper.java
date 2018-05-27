package com.swj.ics.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by swj on 2018/2/26.
 */
public class CuratorHelper {
    public static final String SERVER_IPS = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
    public static final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,10);
    
    private static final int SESSION_TIMEOUT = 15000;
    
    private static CuratorFramework cf ;

    public  static CuratorFramework init() {
        //使用build模式生成 cf 对象
        //1、重试策略，初试时间为1秒，重试次数为10
       
        //2、通过builder模式来创建
        cf = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(SERVER_IPS)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .build();
        //3、开启连接。
        cf.start();
        return cf;
    }
}
