package com.swj.ics.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by swj on 2018/2/26.
 * 使用zk来做为分布式计数器
 */
public class AtomicInteger {
   
    private static CuratorFramework cf = null;

    public static void main(String[] args) throws Exception {
        cf  =  CuratorHelper.init() ;
        //使用DistributedAtomicInteger
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(cf,
                "/super",new RetryNTimes(3,1000));
        AtomicValue<Integer> value = atomicInteger.add(1);
        System.out.println("atomic increment 是否成功：" + value.succeeded());
        System.out.println("pre value : " + value.preValue()); //原始值 ？？
        
        System.out.println("post value : " + value.postValue());//最新指值
    }
    
    
    
    
}
