package com.swj.ics.zookeeper.book;

import java.io.IOException;
import java.io.InterruptedIOException;
import com.swj.ics.MasterWorker.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/25.
 */
public class TestTaskAssignment {
    
    private static final Logger logger = LoggerFactory.getLogger(TestTaskAssignment.class);

    private static final String serverIps = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
    
    @Test(timeout = 50000)
    public void test_task_assignment_sequiential() throws IOException, InterruptedException {
        //先创建并启动master ,然后创建并注册3发个工人，最后由客户端批量提交任务，然后客户单等待批量任务的执行情况
        logger.info("starting master - sequential ");
        Master master = new Master(serverIps);
        master.startZK();
       
        while (!master.isConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        master.bootstrap();
        master.runForMaster();

        logger.info("初始化并穿件3个worker...");
        Worker worker1 = new Worker(serverIps);
        Worker worker2 = new Worker(serverIps);
        Worker worker3 = new Worker(serverIps);

        while (!worker1.isConnected() && !worker2.isConnected() && !worker3.isConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        //bootstrap 创建相关 /assign/worker-节点
        worker1.bootstrap();worker2.bootstrap();worker3.bootstrap();
        worker1.register();
        worker1.getTasks();//监视任务的变化
        
        worker2.register();
        worker2.getTasks();
        
        worker3.register();
        worker3.getTasks();
        
        logger.info("Starting client....");
        
        
        worker1.close();
        worker2.close();
        worker3.close();
        
        master.stopZK();
        
    }
    
    @Test
    public void test_StringToInteger() {
        String value = "110";
        Integer value10 = Integer.valueOf(value);
        Integer value2 = Integer.parseInt(value,2);
        Integer value16 = Integer.valueOf(value,16);
        
        System.out.println(value2);
        System.out.println(value16);
        
        String binaryIntegerValue = Integer.toBinaryString(value10);
        String hexStringValue = Integer.toHexString(value10);

        System.out.println(binaryIntegerValue);
        System.out.println(hexStringValue);
    }
}
