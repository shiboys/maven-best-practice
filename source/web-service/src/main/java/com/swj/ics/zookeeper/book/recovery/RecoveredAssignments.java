package com.swj.ics.zookeeper.book.recovery;

import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by swj on 2018/2/27.
 * 总结起来，这个类的功能是 恢复因主节点崩掉而造成的任务中断
 * 恢复的原理是根据 assign 获取 assignedWorkers和 activeWorkers,根据assign和assignworkers
 * 获取tasks 。这些tasks里面如果有worker已经不在active里面了，assign删除，task重新分配
 * 最后根据 把assign里面属于activeWorkers的task删除，然后再根据status下面的task全部进行删除
 * 然后重新 调用master的getTask()方法进行任务重新分配
 */
public class RecoveredAssignments {
    
    private List<String> thisTasks; //task 结合包括 /task下的所有  + assign/workerxx/task + status/task
    private List<String> activeWorkers;//workers/下的所有的worker
    private List<String> assignedWorkers; // assing/workerxx下的worker工作节点结合
    private List<String> assignmentTaskToBeDeleted; //assign/workerxx/task 集合
    private List<String> statusTaskList ; // status下的任务结合
    
    private ZooKeeper zk = null;
    public RecoveredAssignments(ZooKeeper zk ) {
        this.zk = zk;
        assignmentTaskToBeDeleted = new ArrayList<>();
    }
    
    public void getTasks() {
        zk.getChildren("/task",false,getTaskChildrenCallback,null);
    } 
    AsyncCallback.ChildrenCallback getTaskChildrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> taskChildren) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    thisTasks = taskChildren;
                    getAssignedWorkers();
                    break;
            }
        }
    };

    private void getAssignedWorkers() {
        
    }
}
