package com.swj.ics.zookeeper.curator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.sun.javafx.sg.prism.NodePath;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by swj on 2018/2/26.
 */
public class CuratorBase {
    private static final String SERVER_IPS = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
    private static final int SESSION_TIMEOUT = 15000;
    private CuratorFramework cf ;
    
    void init() {
        //使用build模式生成 cf 对象
        //1、重试策略，初试时间为1秒，重试次数为10
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,10);
        //2、通过builder模式来创建
        cf = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(SERVER_IPS)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .build();
        //3、开启连接。
        cf.start();
    }
    
    void create(String nodePath,String nodeData) throws Exception {
        //不加withMode，默认为持久性节点
        cf.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(nodePath,nodeData.getBytes());
    }
   
    
    void  readAndUpdateData(String nodePath,String newNodeData) throws Exception {
        String oldValue = new String(cf.getData().forPath(nodePath));
        System.out.println("old value for path " + nodePath +" is " + oldValue);
        cf.setData().forPath(nodePath,newNodeData.getBytes());
        String newValue = new String(cf.getData().forPath(nodePath));
        System.out.println("new value for path " + nodePath +" is " + newValue);
    }
    
    void getChildren(String parentNodePath) throws Exception {
        List<String> children = cf.getChildren().forPath(parentNodePath);
        for(String child : children) {
            System.out.println(child);
        }
    }
    
    void existsWithStat(String nodePath) throws Exception  {
        Stat stat = cf.checkExists().forPath(nodePath);
        System.out.println(stat);
    }
    
    void callbackWithThreadPool(ExecutorService pool,String nodePath,String nodeData) throws Exception  {
        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework cf, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("code : " + curatorEvent.getResultCode());
                        System.out.println("type：" + curatorEvent.getType());
                        System.out.println("Thread Name : " + Thread.currentThread().getName());
                    }
                },pool)
                .forPath(nodePath,nodeData.getBytes());
        Thread.sleep(20*1000);
    }

    void delete(String nodePath) throws Exception {
        cf.delete().deletingChildrenIfNeeded()
                .forPath(nodePath);
    }
    
    public static void main(String[] args) throws Exception {
        CuratorBase cb = new CuratorBase();
        cb.init();
        String nodeBasePath = "/super";
        String nodePath1 ="/c1";
        String nodeData1 = "c1内容";
       // cb.create(nodeBaseUrl + nodePath1,nodeData1);
        //System.out.println("done!");

        String nodePath2 =nodeBasePath + "/c2";
        String nodeData2 = "c2内容";
       // cb.create(nodePath2,nodeData2);
        //cb.readAndUpdateData(nodePath2,"c2内容修改");
       // cb.getChildren(nodeBasePath);

        String nodePath3 = nodeBasePath + "/c3";
        String nodeData3 = "c3内容";
       /* ExecutorService threadPool = Executors.newCachedThreadPool();
        cb.callbackWithThreadPool(threadPool,nodePath3,nodeData3);*/

       cb.delete(nodeBasePath);

        System.out.println("done!");
    }
}
