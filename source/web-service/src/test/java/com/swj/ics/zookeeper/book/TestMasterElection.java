package com.swj.ics.zookeeper.book;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/25.
 */
public class TestMasterElection {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestMasterElection.class);
    
    private final String serverIps = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
    
    
    @Test(timeout = 50000)
    public void test_master_election() throws IOException, InterruptedException {
        Master master = new Master(serverIps);
        //获取ZK对象
        master.startZK();
        //主线程等待子线程获取ZK对象
        while (!master.isConnected()) {
            Thread.sleep(500);
        }
        //创建节点
        master.bootstrap();
        master.runForMaster();
        //初始状态为running
        while (master.getState() == Master.MasterState.RUNNING) {
            Thread.sleep(100);
        }
        //循环等待成为主节点
        Assert.assertTrue("Master Not elected",
                master.getState() == Master.MasterState.ELECTED);
        master.stopZK();
    }
    
    @Test(timeout = 50000)
    public void test_reElect_master() throws IOException, InterruptedException {
        Master master = new Master(serverIps);
        //获取ZK对象
        master.startZK();
        //主线程等待子线程获取ZK对象
        while (!master.isConnected()) {
            Thread.sleep(500);
        }
        //创建节点
        master.bootstrap();
        master.runForMaster();
        //初始状态为running
        while (master.getState() == Master.MasterState.RUNNING) {
            Thread.sleep(100);
        }
        //循环等待成为主节点
        Assert.assertTrue("Master Not elected",
                master.getState() == Master.MasterState.ELECTED);
        master.stopZK();



       Master master2 = new Master(serverIps);
        //获取ZK对象
        master2.startZK();
        //主线程等待子线程获取ZK对象
        while (!master2.isConnected()) {
            Thread.sleep(500);
        }
        //创建节点
        master2.bootstrap();
        master2.runForMaster();
        //初始状态为running
        while (master2.getState() == Master.MasterState.RUNNING) {
            Thread.sleep(100);
        }
        //循环等待成为主节点
        Assert.assertTrue("Master Not elected",
                master2.getState() == Master.MasterState.ELECTED);
        master2.stopZK();
    }
    
    
    @Test(timeout = 50000)
    public void elect_single_master() throws IOException, InterruptedException {
        Master master = new Master(serverIps);
        Master master2 = new Master(serverIps);
        //获取ZK对象
        master.startZK();
        master2.startZK();
        //主线程等待子线程获取ZK对象
        while (!master.isConnected() || !master2.isConnected()) {
            Thread.sleep(500);
        }
        //创建节点
        master.bootstrap();
        master2.bootstrap();
        
        
        master.runForMaster();
        master2.runForMaster();
        //初始状态为running
        while (master.getState() == Master.MasterState.RUNNING ||
                master2.getState() == Master.MasterState.RUNNING) {
            Thread.sleep(100);
        }
        boolean isSingleMaster = (master.getState() == Master.MasterState.ELECTED 
                && master2.getState() != Master.MasterState.ELECTED) ||
                (master.getState() != Master.MasterState.ELECTED
                        && master2.getState() == Master.MasterState.ELECTED);
        //循环等待成为主节点
        Assert.assertTrue("Master Not elected",
                isSingleMaster);
        
        Thread.sleep(5000);
        master.stopZK();
        master2.stopZK();
    }
    
    @Test(timeout = 50000)
    public void test_masterExists() throws IOException, InterruptedException  {
        Master master = new Master(serverIps);
        master.startZK();
        while (!master.isConnected() ) {
            Thread.sleep(500);
        };
        master.bootstrap();
        master.existsMaster();
        
        boolean elected = true;
        int counter = 10;
        
        //2秒之后，如果master还没有被创建，则说明当前会话并没有创建成功
        while (master.getState() == Master.MasterState.RUNNING) {
            Thread.sleep(200);
            if (counter-- == 0) {
                LOGGER.info("master exists breaking...");
                elected = false;
                break;
            }
        }
        
        Assert.assertTrue("master not elected",elected);
        
        master.stopZK();
    }
}
