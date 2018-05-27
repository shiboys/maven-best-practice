package com.swj.ics.zookeeper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.expression.spel.ast.OpAnd;
import sun.awt.windows.ThemeReader;

/**
 * Created by swj on 2018/2/13.
 */
public class ZooKeeperWatcher implements Watcher {
    /** 计数器*/
    private AtomicInteger atomicCounter = new AtomicInteger();
    /** zookeeper 的服务器地址*/
    private static final String serverIPs = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
    /** ZK 默认的 超时时间 */
    private static final int SESSION_TIME_OUT = 15000;
    /** 信号量设置，用于等待zookeeper客户端跟服务器建立连接之后，通知阻塞程序继续执行 */
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    /** zk父路径设置*/
    private static final String PARENT_PATH = "/testWatcher";
    /** zk子路径*/
    private static final String CHILDREN_PATH = "/testWatcher/children";
    
    private static final String MAIN_LOG_PREFIX="【Main】";
    
    private  ZooKeeper zk = null;
    
    private static void println(String str) {
        System.out.println(str);
    }
    
    private void createConnection(String serverAddr,int sessionTimeOut) {
        releaseConnection();
        try {
            zk = new ZooKeeper(serverAddr,sessionTimeOut,this);
            //等待回调函数通知当前线程继续
            println(MAIN_LOG_PREFIX + "开始连接ZK服务器。。。");
            countDownLatch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void releaseConnection() {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  7个公共方法
     *  1、createPath
     *  2,readData
     *  3、writeData
     *  4、exists
     * 5、listChildren
     * 6、deleteNode
     * 7、deleteAlTestNode
     */
    
    private Stat exists(String path,boolean watch) {
        try {
            return zk.exists(path,watch);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 创建节点
     * @param parentPath
     * @param data
     * @return 是否创建成功。
     */
    private boolean createPath(String parentPath,String data) {
        //创建之前先设置监控点，这样就可以在process里面接收到通知
        exists(parentPath,true);
        try {
           String resultPath = zk.create(parentPath,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                   CreateMode.PERSISTENT);
           println( MAIN_LOG_PREFIX + "节点创建成功，path ：" + resultPath + ",data:" + data);
           return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private String readData(String path,boolean needWatch) {
        try {
            return new String(zk.getData(path,needWatch,null));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private boolean writeData(String path,String data) {
        try {
           Stat stat = zk.setData(path,data.getBytes(),-1);
           println(MAIN_LOG_PREFIX + "更新数据成功,path="+path+",stat = " + stat);
           return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private List<String> listChildren(String parentPath,boolean needWatch) {
        try {
           return  zk.getChildren(parentPath,needWatch);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    private boolean deleteNode(String path,boolean needWatch) {
        exists(path,needWatch);
        try {
            zk.delete(path,-1);
            println(MAIN_LOG_PREFIX + "节点删除成功，路径为:" + path);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void deleteAllTestNodes(boolean needWatch) {
        try {
            if (exists(CHILDREN_PATH,needWatch) != null) {
                zk.delete(CHILDREN_PATH,-1);
            }
            if (exists(PARENT_PATH,needWatch) != null) {
                zk.delete(PARENT_PATH,-1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    
    
    
    @Override
    public void process(WatchedEvent watchedEvent){
        System.out.println("进入process...! Event is " + watchedEvent);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (watchedEvent == null) {
            return ;
        }
        
        String logPrefix = "【Watcher-" + this.atomicCounter.incrementAndGet() + "】"; 
        Event.KeeperState state = watchedEvent.getState();
        Event.EventType eventType = watchedEvent.getType();
        String path =  watchedEvent.getPath();
        
        println(logPrefix + "收到Watcher通知");
        println(logPrefix + "连接状态:\t" + state);
        println(logPrefix + "事件类型:\t" + eventType);
        
        
        if (state == Event.KeeperState.SyncConnected) {
            if (eventType == Event.EventType.None) {
                println(logPrefix + "成功连接上ZK服务器");
                countDownLatch.countDown();
            } else if (eventType == Event.EventType.NodeCreated) {
                println(logPrefix + "节点创建");
                //创建完之后 加个监控，以便指定节点的状态
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                exists(path,true);
            } else if (eventType == Event.EventType.NodeDataChanged) {
                println(logPrefix + "节点数据更新");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                println(logPrefix + "更新后的节点数据为:" + readData(path,true));
            } else if (eventType == Event.EventType.NodeChildrenChanged) {
                println(logPrefix + "子节点更新");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                println(logPrefix + "子节点列表：" + listChildren(path,true));
            } else if (eventType == Event.EventType.NodeDeleted) {
                println(logPrefix + "节点被删除！path = " + path);
            }
            
        } else if (state == Event.KeeperState.AuthFailed) {
            println(logPrefix + "权限检查失败");
        } else if (state == Event.KeeperState.Disconnected) {
            println(logPrefix + "客户端与ZK服务器断开连接");
        }
        else if (state == Event.KeeperState.Expired) {
            println(logPrefix + "会话过期");
        }
        
        
    }

    public static void main(String[] args) throws Exception {
        ZooKeeperWatcher watcher = new ZooKeeperWatcher();
        watcher.createConnection(serverIPs,SESSION_TIME_OUT);
        Thread.sleep(1000);
        
        //先清理子节点
        watcher.deleteAllTestNodes(true);
        boolean createResult = watcher.createPath(PARENT_PATH,System.currentTimeMillis() + "");
        if (createResult) {
            //读取节点数据，更新节点数据
            println("------------------------------read parent-----------------------");
            String nodeData =  watcher.readData(PARENT_PATH,true);
            println("nodeData is " + nodeData);
            watcher.writeData(PARENT_PATH,System.currentTimeMillis() + "");
                       
            Thread.sleep(1000);
            //创建子节点，读取子节点数据，更新子节点数据
            //将子节点的data跟主节点分开
            Thread.sleep(1000);
            println("------------------------------read children-----------------------");
            watcher.createPath(CHILDREN_PATH,System.currentTimeMillis() + "");
            watcher.listChildren(CHILDREN_PATH,true);
            Thread.sleep(1000);
            watcher.writeData(CHILDREN_PATH,System.currentTimeMillis() + "");
        }
        
        //休眠50s
        Thread.sleep(5000);
        watcher.deleteAllTestNodes(true);
        Thread.sleep(1000);
        watcher.releaseConnection();
    }
}