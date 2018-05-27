package com.swj.ics.zookeeper.book;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import com.swj.ics.MasterWorker.Task;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/25.
 * Client的作用是提交task到 task/xxx下，然后去status/xxx下观察任务的执行情况
 * 
 */
public class Client implements Watcher,Closeable {
    
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    
    private String serverIps;
    private ZooKeeper zk = null;
    private volatile boolean connected = false;
    private volatile boolean expired = false;
    
    public Client(String serverIps) {
        this.serverIps= serverIps;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    void startZK() throws IOException {
        zk = new ZooKeeper(serverIps,15000,this);
    }
    
       
    
    @Override
    public void close() throws IOException {
        logger.info("closing zk session...");
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void process(WatchedEvent e) {
        switch (e.getState()) {
            case SyncConnected:
                connected = true;
                break;
            case Expired:
                connected = false;
                expired = true;
                logger.info("Exiting due to the session expiration ");
                break;
            case Disconnected:
                connected = false;
                break;
        }
    }
    
    static class TaskObject {
        private String taskData;//任务的值data
        private String taskName;//任务的名称，由于任务task的创建采用的是持久有序类型，因此这里使用taskName来记录名称
        private boolean done;
        private boolean successful;
        private CountDownLatch latch = new CountDownLatch(1);

        public String getTaskData() {
            return taskData;
        }

        public void setTaskData(String taskData) {
            this.taskData = taskData;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }
        
        void setStatus(boolean successful) {
            this.successful = successful;
            this.done = true;
            latch.countDown();//通知等待程序可以执行
        }
        
        void waitUntilDone() {
            try {
                latch.wait();
            } catch (InterruptedException e) {
                logger.warn("InterruptedException while waiting task to be done !");
            }
        }
    }
    
    
    void submitTask(String taskData,TaskObject taskObject) {
        taskObject.setTaskData(taskData);
        zk.create("/task/task-",taskData.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL,taskCreateCallback,taskObject
        );
    }
    
    AsyncCallback.StringCallback taskCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            TaskObject taskObject = (TaskObject) ctx;
            switch (code) {
                case CONNECTIONLOSS:
                    submitTask(taskObject.getTaskData(),taskObject);
                    break;
                case OK: //创建成功，能成功拿到name,这时候需要在对应的status/taskName下设立观察点，
                    //一旦观察到有新节点创建，则证明任务已经配分配和执行了。
                    logger.info("my new Created task name is " + name);
                    taskObject.setTaskName(name);   
                    String statusPath = path.replace("/task/","/status/") + name;
                    setStatusWatcher(statusPath,taskObject);
                    break;
                default:
                    logger.error("new task create fail",KeeperException.create(code,path));
                    break;
            }
        }
    };
    
    protected ConcurrentHashMap<String,Object> cacheMap = new ConcurrentHashMap<>();
    
    void setStatusWatcher(String statusPath,Object ctx) {
        cacheMap.put(statusPath,ctx);
        zk.exists(statusPath,statusExistsWatcher,statusNodeExistsCallback,ctx);
    }
    
    AsyncCallback.StatCallback statusNodeExistsCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    setStatusWatcher(path,ctx);
                    break;
                case OK: //节点已经存在
                    if (stat != null) { //说明成功的获取了节点的信息
                        logger.info("status node already exists ,path is " + path);
                        zk.getData(path,false,getStatusDataCallback,ctx);
                    }
                    break;
                case NONODE: //节点还没有被创建
                    break;
                default:
                    logger.error("something went wrong when checking " +
                            " node is exists . path is " + path);
                    break;
            }
        }
    };
    
    
    
    Watcher statusExistsWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent e) {
            if (e.getType() == Event.EventType.NodeCreated) {
                assert e.getPath().contains("/status/task-");
                assert cacheMap.containsKey(e.getPath());
                //Event.KeeperState.
                
                zk.getData(e.getPath(),null,
                        getStatusDataCallback,cacheMap.get(e.getPath()));
            }
            
        }
    };
    
    AsyncCallback.DataCallback getStatusDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    zk.getData(path,null,
                            getStatusDataCallback,cacheMap.get(path));
                    break;
                case OK:
                    //首先 通过data 是否等于 done 判断，使用 TaskObject 对象更改通知，通知客户端，
                    //通知完成之后，将status node data 删除
                    String nodeData = new String(bytes);
                    logger.info("task " + path +", handle result is " + nodeData);
                    
                    assert ctx != null;
                    TaskObject taskObject = (TaskObject) ctx;
                    taskObject.setStatus(nodeData.contains("done"));
                    zk.delete(path,-1,statusNodeDelCallback,ctx);
                    cacheMap.remove(path);
                    break;
                case NONODE:
                    logger.warn("status code has been deleted ! path is " + path);
                    break;
                default:
                    
                    break;
            }
        }
    };
    
    AsyncCallback.VoidCallback statusNodeDelCallback = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    zk.delete(path,-1,statusNodeDelCallback,ctx);
                    break;
                case OK:
                    logger.info("status node was deleted ! path is " + path);
                    break;
                default:
                    logger.error("something went wrong when deleting the status node and path is " + path);
                    break;
            }
        }
    };
    
    
}
