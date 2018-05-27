package com.swj.ics.zookeeper.curator.barrier;

import java.util.Random;
import com.rabbitmq.client.AMQP;
import com.swj.ics.zookeeper.curator.CuratorHelper;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/26.
 * CuratorBarrier作为Barrie屏障来使用的使用示例
 */
public class CuratorBarrierAsBarrier {
    private static final Logger logger = LoggerFactory.getLogger(CuratorBarrierAsBarrier.class);
    private static final int THREAD_COUNT = 5;
   
    private static CuratorFramework cf;
    
    public static void main(String[] args) {
         
        cf = CuratorFrameworkFactory.builder()
                .connectString(CuratorHelper.SERVER_IPS)
                .retryPolicy(CuratorHelper.retryPolicy)
                .build();
        cf.start();
       
        for(int i = 0 ; i < 5; i++) {
            startThreadToRunCuratorBarrier("t" + i);
        }
    }
    
    private static void startThreadToRunCuratorBarrier(String threadName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //先准备，然后开始同时运行，最后同时推出
                try {
                    DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(cf,"/super",THREAD_COUNT);
                    
                    Thread.sleep(1000 * (new Random()).nextInt(3));
                    logger.info("{}已结准备",Thread.currentThread().getName());
                    barrier.enter();
                    logger.info("同时开始运行。。。");
                    //模拟不同的线程运行时间
                    Thread.sleep(1000 * (new Random()).nextInt(3));
                    logger.info("运行完毕。。。");
                    barrier.leave();
                    logger.info("同时退出运行。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },threadName).start();
    }
}
