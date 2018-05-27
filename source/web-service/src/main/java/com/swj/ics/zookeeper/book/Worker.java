package com.swj.ics.zookeeper.book;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
 */
public class Worker implements Watcher,Closeable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
    
    private ZooKeeper zk;
    private String serverIps = "";
    private String serverId = Integer.toHexString(new Random().nextInt());
    private volatile boolean connected = false;
    private volatile boolean expired = false;
    
    private ThreadPoolExecutor threadPool;
    protected ChildrenCache assignTaskCache = new ChildrenCache();
    
    
    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    public Worker(String serverIps) {
        this.serverIps = serverIps;
        threadPool = new ThreadPoolExecutor(1,1,
                1000L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(200)
                );
    }
    
    void startZK() throws IOException {
        zk = new ZooKeeper(serverIps,15000,this);
    }
    
        
    @Override
    public void close() throws IOException {
        LOGGER.info("closing worker zk....");
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                LOGGER.warn("zookeeper interrupted while closing...",e);
            }
        }
    }

    @Override
    public void process(WatchedEvent e) {
        if (e.getType() == Event.EventType.None) {
            switch (e.getState()) {
                case SyncConnected:
                    connected = true;
                    break;
                case Expired:
                    expired =true;
                    connected = false;
                    LOGGER.error("Session expired");
                    break;
                case Disconnected:
                    connected = false;
                    break;
            }
        }
    }
    
    void bootstrap() {
        createAssignNode();
    }
    
    void createAssignNode() {
        zk.create("/assign/worker-" + this.serverId,
                new byte[0],
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                createAssignCallback,
                null);
    }
    
    AsyncCallback.StringCallback createAssignCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    createAssignNode();
                    break;
                case OK:
                    LOGGER.info("assign node was created ! path is "+ path);
                    break;
                case NODEEXISTS:
                    LOGGER.info("assign node was already exists ! path is "+ path);
                    break;
                default:
                    LOGGER.info("assign node creating went wrong ! path is "+ path);
                    break;
            }
        }
    };
    
    //create assign
    //create worker / register worker .从节点任何时候都能添加进来
    //Methods to wait for new assignments 。从 getTasks 开始。
    // 思路是　task　从　assign/worker-xxx/task1-taskN。
    //如果getTask()的回调函数能get到task，说明是新的任务来了，则更新当前worker的状态为working
    //作法是使用线程池的技术将 workerxx的value更新为 working.由于是在多线程环节中，zk.update需要增加synchronized
    //获取到task,使用线程池来执行task,执行的过程是增加 status/taskx ,value = "done"
    //节点增加完之后，将原来的 assignment 删除
    
    String name;

    /**
     * 向zookeeper 的 /workers/注册子节点
     */
    void register() {
        name = "worker-" + serverId;
        //所有的worker都是临时工
        zk.create("/workers/"+name,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,
                workerCreateCallback,null);
    }
    AsyncCallback.StringCallback workerCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code= KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    LOGGER.info("worker register successfully! path is " + path);
                    break;
                default:
                    LOGGER.info("worker registration went wrong ! path is " + path);
                    break;
            }
        }
    };

    /**
     * 从 assign/worker-xx那里 获取所有主节点分配的任务（又叫子节点）
     */
    void getTasks() {
        zk.getChildren("/assign/worker-" + this.serverId,assignNewTaskWatcher,
                assignTaskGetCallback,null);
    }

    /**
     * 在 /assign/worker-"+this.serverId 加一个监视点，如果该节点下有新增的子节点，也就代表有新的任务，需要
     * 重新调用 getTask()以便任务能够及时被处理或者运行。
     */
    Watcher assignNewTaskWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent e) {
            assert e.getPath().equals("/assign/worker-" + serverId);
            getTasks();
        }
    };
    
    AsyncCallback.ChildrenCallback assignTaskGetCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK: //如果有新任务，使用线程池去获取每个新任务的具体情况
                    if(children == null || children.size() < 1) {
                        LOGGER.info("no new task found for path " + path);
                        break;
                    }
                    LOGGER.info("getting new tasks for path " + path);
                    threadPool.execute(new Runnable() {
                        List<String> taskList;
                        DataCallback getTaskDataCallback ;
                        Runnable init(List<String> taskList,DataCallback cb) {
                            this.taskList = taskList;
                            this.getTaskDataCallback = cb;
                            return this;
                        }  
                        @Override
                        public void run() {
                            if (taskList == null || taskList.size() < 1) {
                                return ;
                            }
                            
                            for(String task : taskList) {
                                //把当前的任务当做参数传入回调函数
                                zk.getData(path+"/" + task,false,getTaskDataCallback,task);
                            }
                            
                        }
                    }.init(assignTaskCache.addedAndSet(children),getTaskDataCallback));
                    break;
                default:
                    LOGGER.info("get the assign task went wrong, parent path is " + path,
                            KeeperException.create(code,path));
            }
        }
    };
    
    AsyncCallback.DataCallback getTaskDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    zk.getData(path,false,getTaskDataCallback,ctx);
                    break;
                case OK: //成功获取到任务task的数据
                    //这时候就需要分配任务了，具体的分配过程分2步
                    //1、创建 节点 status/task,值为"done"
                    //2、删除任务分配对象， assignment 节点,路径是 /assign/worker-serverId/task
                    threadPool.execute(new Runnable() {
                        Object _ctx;//里面存储了path
                        byte[] _data; //打印日志信息,里面存储的task的data信息
                        Runnable init(Object ctx,byte[] bytes1) {
                         this._ctx = ctx;
                         _data = bytes1;
                         return this;
                        }
                        
                        @Override
                        public void run() {
                            LOGGER.info("beginning to handle task " + new String(_data));
                            zk.create("/status/"+ (String)_ctx, "done".getBytes(), 
                                    ZooDefs.Ids.OPEN_ACL_UNSAFE,  CreateMode.PERSISTENT,//这个是永久节点
                                    statusDataCreateCallback, _ctx
                                    );
                            //删除assignment
                            zk.delete("/assign/worker-" + serverId + "/" + (String)_ctx,-1,assignmentDeleteCallback,_ctx);   
                        }
                    }.init(ctx,bytes));
                    break;
                default:
                    LOGGER.error("getting task data went wrong,path is " + path,
                            KeeperException.create(code,path));
                    break;
            }
        }
    };
    
    AsyncCallback.StringCallback statusDataCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    zk.create("/status/"+ (String)ctx, "done".getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE,  CreateMode.PERSISTENT,//这个是永久节点
                            statusDataCreateCallback, null
                    );
                    break;
                case OK:
                    LOGGER.info("create status node successfully , path is " + path);
                    break;
                case NODEEXISTS:
                    LOGGER.info("status node already exists , path is " + path);
                    break;
                default:
                    LOGGER.info("Failed to create status code ,, path is " + path);
                    break;
            }
        }
    };
    
    AsyncCallback.VoidCallback assignmentDeleteCallback = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object o) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    zk.delete(path,-1,assignmentDeleteCallback,o);
                    break;
                case OK:
                    LOGGER.info(" assign node was deleted successfully ! path is " + path);
                    break;
                default:
                    LOGGER.error(" failed to delete assign node ! path is " + path);
                    break;
            }
        }
    };
    
    public static void main(String[] args) throws IOException {
        String serverIps = "192.168.0.106:2181,192.168.0.107:2181,192.168.0.108:2181";
        
        Worker worker = new Worker(serverIps);
        
        long after15SecondsFromNow = System.currentTimeMillis() + 30 * 1000;
        
        worker.startZK();
        //创建必要的任务分配父节点
        worker.bootstrap();
        //注册到队列里面，让主节点可以取到该worker
        worker.register();
        //获取主节点分配的任务，并运行任务
        worker.getTasks();
        
        //运行15秒
        while (!worker.isExpired() && after15SecondsFromNow > System.currentTimeMillis()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        worker.close();
    }
}
