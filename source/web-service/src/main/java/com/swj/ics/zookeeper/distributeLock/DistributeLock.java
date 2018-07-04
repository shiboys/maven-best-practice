package com.swj.ics.zookeeper.distributeLock;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import com.swj.ics.zookeeper.curator.CuratorHelper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by swj on 2018/6/18.
 */
public class DistributeLock implements Lock, Watcher {
    private static final String ZK_SERVER_IP = CuratorHelper.SERVER_IPS;
    private static final String ROOT_LOCK = "/rootLocks";
    private String current_lock = null;
    private String wait_lock = null;//标识等待的上一个锁
    private ZooKeeper zk = null;

    private CountDownLatch countDownLatch;


    public DistributeLock() {
        try {
            zk = new ZooKeeper(ZK_SERVER_IP, 15000, this);
            Stat stat = zk.exists(ROOT_LOCK, false);
            if (stat == null) {
                zk.create(ROOT_LOCK, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tryLock() {
        try {
            current_lock = zk.create(ROOT_LOCK+"/","0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(getCurrentThreadName(current_lock) +"-->尝试获取锁");
            
            List<String> childrenPath = zk.getChildren(ROOT_LOCK,false);
            SortedSet<String> sortedSet = new TreeSet<>();
            for(String child : childrenPath) {
                sortedSet.add(ROOT_LOCK + "/" + child);
            }
            String firstLock = sortedSet.first();
            if(firstLock.equals(current_lock)) {
                System.out.println("当前线程"+getCurrentThreadName(current_lock)+"获取锁");
                return true;
            }
            //否则找出当前节点的上一个节点，在上一个节点上添加一个检测
            TreeSet<String> lessThanMeList = (TreeSet<String >)sortedSet.headSet(current_lock);
            if(lessThanMeList != null && lessThanMeList.size() > 0) {
                wait_lock = lessThanMeList.last();
            }
            
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void lock() {
        if (tryLock()) {
            return;
        }
        try {
            waitForLock(wait_lock);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  catch (KeeperException e) {
            e.printStackTrace();
        }

    }
    
    private void waitForLock(String prevNode) throws InterruptedException, KeeperException {
        //没有获取锁，则继续等待
       Stat stat = zk.exists(prevNode,true);
       if (stat != null) {
           System.out.println("当前线程" + Thread.currentThread().getName() + "等待在"+ prevNode);
           countDownLatch = new CountDownLatch(1);
           countDownLatch.await();
           //todo:这里需要继续判断是否当前的锁节点为最小节点
           System.out.println("当前线程" + Thread.currentThread().getName() +"获取锁成功");
       }
    }

    private String getCurrentThreadName(String currentPath) {
        return Thread.currentThread().getName() + "->" + currentPath;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        if(zk != null) {
            try {
                zk.delete(current_lock,-1);
                current_lock = null;
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
