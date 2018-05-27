package com.swj.ics.zookeeper.book;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Random;
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
 * Created by swj on 2018/2/23.
 */
public class Master implements Watcher,Closeable {
    
    private static final Logger LOGGER =  LoggerFactory.getLogger(Master.class);
    
    enum MasterState {RUNNING,ELECTED,NOTELECTED}
    
    private volatile MasterState state = MasterState.RUNNING;
    private volatile boolean connected = false;
    private volatile boolean expired = false;
    private String hostPort;
    private Random random = new Random(this.hashCode());
    private String serverId = Integer.toHexString(random.nextInt());
    private ZooKeeper zk;
    
    protected ChildrenCache workersCache;
    protected ChildrenCache tasksCache;
    
    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    public MasterState getState() {
        return state;
    }

    void startZK() throws IOException {
        zk = new ZooKeeper(hostPort,15000,this);
    }
    
    void stopZK() throws InterruptedException {
        if (zk != null) {
            zk.close();
        }
    }
    
    void bootstrap() {
        createParent("/workers",new byte[0]);
        createParent("/tasks",new byte[0]);
        createParent("/assign",new byte[0]);
        createParent("/status",new byte[0]);
    }
    void createParent(String path,byte[] data) {
        zk.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                CreateMode.PERSISTENT,createParentCallback,data);
    }
    
    AsyncCallback.StringCallback createParentCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String path, Object ctx, String s1) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    createParent(path,(byte[])ctx);
                    break;
                case OK:
                    break;
                case NODEEXISTS:
                    LOGGER.warn("parent node already exists ! path is " + path);
                    break;
                default:
                    LOGGER.error("Something went wrong when create parent node ",
                            KeeperException.create(KeeperException.Code.get(i),path));
            }
        }
    };
    
    @Override
    public void process(WatchedEvent watchedEvent) {
        LOGGER.info("Processing Master event : " + watchedEvent);
        Event.EventType eventType = watchedEvent.getType();
        if (eventType == Event.EventType.None) {
            Event.KeeperState state = watchedEvent.getState();
            switch (state) {
                case SyncConnected:
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    connected = false;
                    expired =true;
                    LOGGER.error("session expired !");
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    void runForMaster() {
        LOGGER.info("Running/Creating for /master");
        zk.create("/master",
                serverId.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                masterCreateCallback,
                null
                );
    }
    
    AsyncCallback.StringCallback masterCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    checkMaster();
                    break;
                case OK:
                    takeLeaderShip();
                    state = MasterState.ELECTED;
                    break;
                case NODEEXISTS:
                    existsMaster();
                    state = MasterState.NOTELECTED;
                    break;
                default:
                    state = MasterState.NOTELECTED;
                    LOGGER.error("Something went wrong when creating/running for master",
                            KeeperException.create(code,path)
                            );
            }
            LOGGER.info("i am " + (state == MasterState.ELECTED ? "" : "not") +" the leader " + serverId);
        }
    };

    private void takeLeaderShip() {
        LOGGER.info("Going list of workers");
        getWorkers();
        
        //todo: 使用 RecoveredAssignments来生成tasks
    }

    

    /**
     * 当master节点存在时
     */
    void existsMaster() {
        zk.exists("/master",
                masterExistsWatcher,
                masterExistsCallback,
                null
                );
    }
    Watcher masterExistsWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            //如果master节点被删除，则这里会接收到通知，并重新创建master
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                //在Java中，assert关键字是从JAVA SE 1.4 引入的，为了避免和老版本的Java代码中使用了assert关键字导致错误，Java在执行的时候默认是不启动断言检查的（这个时候，所有的断言语句都 将忽略！），
                // 如果要开启断言检查，则需要用开关-enableassertions或-ea来开启。
                //如果<boolean表达式>为true，则程序继续执行。
                //如果为false，则程序抛出AssertionError，并终止执行。
                //RUN -> Edit Configurations -> Configuration -> VM options : 输入-ea，点击确定。
                assert "/master".equals(watchedEvent.getPath());
                
                runForMaster();
            }
        }
    };
    
    AsyncCallback.StatCallback masterExistsCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    existsMaster();
                    break;
                case OK:
                    break;
                case NONODE:
                    state = MasterState.RUNNING;
                    runForMaster();
                    LOGGER.info("it sounds like the previous master node is gone," +
                            "so let's run for master again"
                    );
                    break;
                default: //其他情况，比如回话过期等，需要重新check master
                    checkMaster();
                    break;
            }
        }
    };
    /**
     * 当客户端连接/回话断开重连时，通过checkMaster以及其回调能够重新选取/创建master节点
     */
    private void checkMaster() {
        zk.getData("/master",false,masterCheckCallback,null);
    }

    
    AsyncCallback.DataCallback masterCheckCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    checkMaster();
                    break;
                case NONODE:
                    runForMaster();
                    break;
                case NODEEXISTS:
                    if (serverId.equals(new String(data))) {
                        takeLeaderShip();
                    } else {
                        existsMaster();//节点存在，则加监控，以便节点删除的时候得到通知，重新生成节点。
                    }
                    break;
                default:
                    LOGGER.info("Error when reading master data",
                            KeeperException.create(code,path)
                            );
                    break;
            }
        }
    };


    private void getWorkers() {
      zk.getChildren("/workers",workersChangeWatcher,workerChildrenCallback,null);  
    }

    /**
     * 观察如果 /worker children 如果有改变，则需要重新调用getWorkers,重新获取workers
     */
    Watcher workersChangeWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                assert  "/workers".equals(watchedEvent.getPath());
                
                getWorkers();
            }
        }
    };
    
    AsyncCallback.ChildrenCallback workerChildrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    getWorkers();
                    break;
                case OK:
                    LOGGER.info("successfully get a list of workers. its size is " + children.size());
                    reAssignAndSetWorker(children);
                    break;
                default:
                    LOGGER.error("failed to get workers children",
                            KeeperException.create(code,path)
                            );
            }
        }
    };

    private void reAssignAndSetWorker(List<String> children) {
        List<String> toBeProcessed = null;
        //如果是首次加载，则填满cache,否则出现集合替换，则被替换的集合元素放入tobeProcessed，方便以后的处理
        if (workersCache == null) {
            workersCache = new ChildrenCache(children);
        } else {
            LOGGER.info("removing and setting worker children");
            toBeProcessed = workersCache.removedAndSet(children);
        }
        if (toBeProcessed == null) {
            return;
        }
        for(String worker : toBeProcessed) {
            getAbsentWorkerTasks(worker);
        }
    }

    /**
     * 获取那些worker被替换掉的所包含的task列表
     * @param worker
     */
    private void getAbsentWorkerTasks(String worker) {
        zk.getChildren("/assign/"+worker,false,assignmentWorkerCallback,worker);
    }
    
    //获取这些woker所包含的任务，然后把这些任务重新分配
    AsyncCallback.ChildrenCallback assignmentWorkerCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            String worker = ctx.toString();
            switch (code) {
                //todo:注意下这里，这里的Path就是 "/assign/"+worker，而child path 是集合里面的子元素
                case CONNECTIONLOSS:
                    getAbsentWorkerTasks(worker);
                case OK: //获取成功，把children所包含的重新进行分配。
                    LOGGER.info("成功获取workers children,size is " + children.size());
                    for(String task : children) {
                        getAndSetReAssign(path + "/" + task,task);
                    }
                    break;
                default:
                    LOGGER.error("get worker children failed ",
                            KeeperException.create(code,path)
                            );
                    break;
            }
        }
    };

    private void getAndSetReAssign(String path, String task) {
        zk.getData(path,//assign/worker/task
                false,
                getDataReAssignCallback,
                task
                );
    }
    
    AsyncCallback.DataCallback getDataReAssignCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    getAndSetReAssign(path,(String)ctx);
                    break;
                case OK:
                    reCreateTask(new RecreateTaskCtx(path,(String)ctx,bytes));
                    break;
                default:
                    LOGGER.error("something went wrong when get the assgin data",
                            KeeperException.create(code,path)
                            );
            }
        }
    };
    
    

    class  RecreateTaskCtx {
        String path;
        String task;
        byte[] data;

        RecreateTaskCtx(String path,String task,byte[] data) {
            this.data = data;
            this.path = path;
            this.task = task;
        } 
    }
    
    void reCreateTask(RecreateTaskCtx ctx) {
        zk.create("/task/" + ctx.task,
                ctx.data, 
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                recreateTaskCallback,
                ctx
                );
    }
    
    AsyncCallback.StringCallback recreateTaskCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            RecreateTaskCtx rtCtx = (RecreateTaskCtx)ctx;
            switch (code) {
                case CONNECTIONLOSS:
                    reCreateTask(rtCtx);
                    break;
                case OK:
                    deleteAssignment(rtCtx.path);
                    break;
                    //todo：感觉这里是死循环呀
                case NODEEXISTS:
                    LOGGER.warn("task Node already exists,but it hasn't been deleted,so we will keep trying");
                    reCreateTask(rtCtx);
                    break;
                default:
                    LOGGER.error("something went wrong when recreating the task",
                            KeeperException.create(code,path));
                    break;
            }
        }
    };

    private void deleteAssignment(String path) {
        zk.delete(path,-1,assignmentDeleteCallback,null);
    }
    
    AsyncCallback.VoidCallback assignmentDeleteCallback = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object o) {
            KeeperException.Code code = KeeperException.Code.get(rc);
          
            switch (code) {
                case CONNECTIONLOSS:
                    deleteAssignment(path);
                    break;
                case OK:
                  LOGGER.info("assignment was deleted successfully. path is " + path);
                    break;
              
                default:
                    LOGGER.error("something went wrong when deleting assignment,path is " + path,
                            KeeperException.create(code,path));
                    break;
            }
        }
    };


    @Override
    public void close() throws IOException {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                LOGGER.warn("InterruptedException while closing zookeeper master client");
            }
        }
    }

    /**
     *  getTasks() 最终目的是获取task列表，然后进行分配，具体过程分为如下步骤
     *  1、getTask 获取task列表，对该列表实现了缓存输出，根据taskCache获取待分配的任务列表toProcess
     *  2、在assignTasks方法里面遍历列表，针对每一个task,从 workerCache里面随机选取一个空闲的workers,
     *  然后 创建节点 assign/worker/taskxxx，data为task/taskxx里面的data,表示把任务放到分配队列里面。
     *  3、分配成功之后，删除task/taskxx节点，表示任务已经分配。防止下次getTask仍然能获取到task.
     */
    void getTasks() {
        zk.getChildren("/task",getTasksWatcher,getTaskChildrenCallback,null);
    }
    Watcher getTasksWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent e) {
            //如果 /task 子节点有变动，则重新获取tasks,因此这里监控的事件类型是 childnodechange
            if (e.getType() == Event.EventType.NodeChildrenChanged) {
                assert  "/task".equals(e.getPath());
                
                getTasks();
            }
        }
    };
    
    AsyncCallback.ChildrenCallback getTaskChildrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    List<String> toProcess;
                    if (tasksCache == null) {
                        tasksCache = new ChildrenCache(children);
                        toProcess = children;
                    } else {
                        toProcess = tasksCache.addedAndSet(children);//如果本次children的数据跟cache完全相同，
                        //则toProcess返回null,否则返回本次和缓存差异的task
                    }
                    if (toProcess != null) {
                        assignTasks(toProcess);
                    }
                    break;
                default:
                    LOGGER.error("failed to get /task children ",KeeperException.create(code,path));
                    break;
            }
        }
    };

    private void assignTasks(List<String> toProcess) {
        for(String task : toProcess) {
            zk.getData("/task/"+task,false,getTaskDataCallback,task);
        }
    }
    
    AsyncCallback.DataCallback getTaskDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            String task = (String) ctx;
            switch (code) {
                case CONNECTIONLOSS:
                    zk.getData("/task/"+task,false,getTaskDataCallback,task);;
                    break;
                case OK: //成功获取到taskData
                    //从workerCache里面随机选取一个worker
                    List<String> workerList = workersCache.getList();
                    //designated 指定的 ，选定的；
                    String designatedWorker = workerList.get(random.nextInt(workerList.size())); 
                    String assignmentPath = "/assign/" + designatedWorker + "/" + task;
                    LOGGER.info("new Assignment and its path is " + assignmentPath);
                    createNewAssignment(assignmentPath,data);
                    break;
                default:
                    break;
            }
        }
    };

    private void createNewAssignment(String assignmentPath, byte[] data) {
        zk.create(assignmentPath,data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,assignmentCreateCallback,data);
    }
    
    AsyncCallback.StringCallback assignmentCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) { 
                case CONNECTIONLOSS:
                    createNewAssignment(path,(byte[])ctx);
                    break;
                case OK:
                    LOGGER.info("assignment create successfully and path is " + path);
                    //任务分配成功之后，启动删除 task 工作
                    //todo:name和 path 相等？
                    deleteTask(name.substring(path.lastIndexOf("/") + 1));
                    break;
                case NODEEXISTS:
                    LOGGER.info("task has already been created ! path is " + path);
                    break;
                default:
                    LOGGER.error("Error when creating the task node ",KeeperException.create(code,path));
                    break;
            }
        }
    };
    
    
    void deleteTask(String taskName) {
        String taskPath = "/task/" + taskName;
        zk.delete(taskPath,-1,deleteTaskCallback,taskName);
    }
    
    AsyncCallback.VoidCallback deleteTaskCallback = new AsyncCallback.VoidCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    deleteTask((String)ctx);
                    break;
                case OK:
                    LOGGER.info("task node was deleted successfully ,path is " + path);
                    break;
                case NONODE:
                    LOGGER.info("task node has been already deleted ,path is " + path);
                    break;
                default:
                    LOGGER.error("Failed to delete task node ,path is " + path);
                    break;
            }
        }
    };

    public static void main(String[] args) {
        LOGGER.info("main class");
        System.out.print("test log4j");
    }
}
