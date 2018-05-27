package com.swj.ics.zookeeper.curator.barrier;

import java.util.Random;
import com.swj.ics.zookeeper.curator.CuratorHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/26.
 * CuratorBarrier作为Countdownlatch来使用的使用示例
 */
public class CuratorBarrierAsCountDownLatch {
    private static final Logger logger = LoggerFactory.getLogger(CuratorBarrierAsCountDownLatch.class);
    private static final int THREAD_COUNT = 5;
   
    private static CuratorFramework cf;
    
    private static DistributedBarrier barrier = null;
    
    public static void main(String[] args) throws Exception {
         
        cf = CuratorHelper.init();
       
        for(int i = 0 ; i < 5; i++) {
            startThreadToRunCuratorBarrier("t" + i);
        }
        Thread.sleep(10000);
        barrier.removeBarrier();
    }
    
    private static void startThreadToRunCuratorBarrier(String threadName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //先准备，然后开始同时运行，最后同时推出
                try {
                     barrier = new DistributedBarrier(cf,"/super");
                     
                    logger.info("{}设置barrier",Thread.currentThread().getName());
                    barrier.setBarrier();
                    logger.info("{}等待",Thread.currentThread().getName());
                    barrier.waitOnBarrier();
                    logger.info("{}子线程开始执行。。。。",Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },threadName).start();
    }
}
