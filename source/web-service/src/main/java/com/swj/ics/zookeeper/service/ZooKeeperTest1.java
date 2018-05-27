package com.swj.ics.zookeeper.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by swj on 2018/2/10.
 */
public class ZooKeeperTest1 {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    static  final  String connectionStr = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
    static final int SESSION_TIMEOUT = 5000;//ms
    
    
    public static void main(String[] args) throws Exception {

        ZooKeeper zooKeeper = new ZooKeeper(connectionStr, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //获取事件的状态
               Event.KeeperState keeperState = watchedEvent.getState();
               //事件的类型
               Event.EventType eventType = watchedEvent.getType();
               
               if(keeperState == Event.KeeperState.SyncConnected) {
                   if (eventType == Event.EventType.None) { //可选的状态类型有，create,update,等。
                       /*
                       * None(-1),
            NodeCreated(1),
            NodeDeleted(2),
            NodeDataChanged(3),
            NodeChildrenChanged(4);
                       * */
                       System.out.println("zookeeper connected");
                       countDownLatch.countDown();
                   }
               }
            }
        });

        countDownLatch.await();
        
       // System.out.println("开始执行创建节点...");
        
        String pathKeyWords = "/testRoot";
        
       /* Stat existsStat = zooKeeper.exists(pathKeyWords,false);
        if (existsStat == null) { //创建
           String ret = zooKeeper.create(pathKeyWords,"testRoot".getBytes(), 
                   ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
           System.out.println("创建完成，返回值是：" + ret);
        } else {
            zooKeeper.setData(pathKeyWords,"testRoot100".getBytes(),0);
        }
        
        //创建临时节点,回话结束就被删除。
        pathKeyWords = "/testRoot/tempData";

        String ret = zooKeeper.create(pathKeyWords,"tempData".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建完成，返回值是：" + ret);*/
        
        
        
       
        
        //1、先异步创建节点
        //2、异步删除节点
        //3、访问节点并打印节点的数据
        //5、set节点，相当于节点的update
        //6、exists 节点
        
        String asyncNodePath = "/asyncNodePath";
        String asyncNodeValue = "asyncValue";
        
       /*createNodeAsyncDemo(asyncNodePath,asyncNodeValue,zooKeeper);
        Thread.sleep(5000);
        //打印异步节点
        getNodeData(asyncNodePath,zooKeeper);
        
       System.out.println("异步删除节点");
       deleteNodeAsyncDemo(asyncNodePath,zooKeeper);

        Thread.sleep(5000);*/

        //getChildrenNodeData(pathKeyWords,zooKeeper);
        
       // setNodeDataDemo(pathKeyWords,"newValue20180212",zooKeeper);

        //getChildrenNodeData("/testRoot/a1",zooKeeper);

        getChildrenNodeDataWithCallback("/testRoot/a1",zooKeeper);
        Thread.sleep(2000);
        zooKeeper.close();
    }

    /**
     * 异步的创建节点demo
     */
    private static void createNodeAsyncDemo(String nodePath,String dataValue,ZooKeeper zooKeeper) {
        zooKeeper.create(nodePath, dataValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
                    @Override
                    public void processResult(int i, String s, Object o, String s1) {
                        //i 为state 0表示创建成功，其他请参考有道笔记
                        if (i == 0) {
                            System.out.println("异步创建节点成功");
                            System.out.println("s = " + s);
                            System.out.println("o = " + o);
                            System.out.println("s1 = " + s1);
                        } else {
                            System.out.println("异步创建失败");
                        }
                        
                    }
                },"ctx");
    }
    
    private static void deleteNodeAsyncDemo(String nodePath,ZooKeeper zooKeeper) {
        zooKeeper.delete(nodePath, -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int i, String s, Object o) {
                System.out.println(String.format("节点删除，当前删除的节点为%s,state= %d,o is %s",s,i,o.toString()));
            }
        },"haha");
    }
    
    private static void getNodeData(String nodePath,ZooKeeper zooKeeper) {
        try {
            byte[] bytes = zooKeeper.getData(nodePath,false,null);
            System.out.println(new String(bytes));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void getChildrenNodeData(String parentNodePath,ZooKeeper zooKeeper) {
        try {
            List<String> childNodePathList = zooKeeper.getChildren(parentNodePath,false);
            for(String path : childNodePathList) {
                System.out.println("child path is " + path);
                System.out.println(new String(zooKeeper.getData(parentNodePath+"/" +  path,false,null)));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getChildrenNodeDataWithCallback(String parentNodePath,ZooKeeper zooKeeper) {
        zooKeeper.getChildren(parentNodePath,false,childrenCallback,null);  
    }
    
    static AsyncCallback.ChildrenCallback childrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object o, List<String> list) {
            System.out.println("path is " + path);       
            for(String childPath : list) {
                System.out.println("child path is " + childPath);
               // System.out.println(new String(zooKeeper.getData(parentNodePath+"/" +  path,false,null)));
            }
        }
    };
    
    
    
    private static void setNodeDataDemo(String nodePath,String newValue,ZooKeeper zooKeeper) {
        try {
            String oldValue = new String( zooKeeper.getData(nodePath,false,null) );
            zooKeeper.setData(nodePath,newValue.getBytes(),-1);
            String newNodeValue =  new String( zooKeeper.getData(nodePath,false,null) );
            System.out.println(String.format("oldValue:%s,newValue:%s",oldValue,newNodeValue));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
}
