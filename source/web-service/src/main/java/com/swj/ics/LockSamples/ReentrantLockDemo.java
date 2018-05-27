package com.swj.ics.LockSamples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import com.swj.ics.netty_study.helloworld.ClientHandler;
import javafx.stage.StageStyle;

/**
 * Created by swj on 2018/1/14.
 * ReentrantLock 可重入锁。
 * 重入是指任意线程在获取锁之后能够再次获取该锁而不会被该锁阻塞
 */
public class ReentrantLockDemo {
    
    //定义一个ReentrantLock2 ，用来实现公平锁和非公平锁。
    //公平锁由于采用FIFO队列，因此这列进行reverse转换
    private static class ReentrantLock2 extends ReentrantLock {
        public ReentrantLock2(boolean fair) {
            super(fair);
        }
        
        //获取当期正在等待获取锁的队列
        public Collection<Thread> getQueuedThread() {
            List<Thread> threadList = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(threadList);
            return threadList;
        }
    }
    
    private static class Job extends Thread {
        
        private Lock lock;
        
        public Job(Lock lock) {
            this.lock = lock;
        }
        public Job(Lock lock,String name) {
            this.lock = lock;
            this.setName(name);
        }
        
        @Override
        public void run() {
            ReentrantLock2 lock2 = (ReentrantLock2)lock;
            if (lock2 == null) {
                System.out.println("lock instance is not type of ReentrantLock2");
                return ;
            }
            //联系2次打印当前的锁的获取情况
            lock2.lock(); 
            try {
                
                System.out.println(String.format("%s, Lock by [%s],Waiting by [%s]",
                        lock2.isFair() ? "fair lock" : "not fair lock",
                        Thread.currentThread().getName(),
                        String.join(",", lock2.getQueuedThread()
                                .stream()
                                .map(x->x.getName())
                                .collect(Collectors.toList())
                        )));
            } finally {
                lock2.unlock();
            }


            lock2.lock();
            try {

                System.out.println(String.format("%s, Lock by [%s],Waiting by [%s]",
                        lock2.isFair() ? "fair lock" : "not fair lock",
                        Thread.currentThread().getName(),
                        String.join(",", lock2.getQueuedThread()
                                .stream()
                                .map(x->x.getName())
                                .collect(Collectors.toList())
                        )));
            } finally {
                lock2.unlock();
            }
            
        }
    }

    public static void testLock(Lock lockPara) {
        //启动5个Job来执行
        for(int i = 0; i < 5 ;i++) {
            Job job = new Job(lockPara,i+"");
            job.start();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Lock lockFair = new ReentrantLock2(true);
        Lock notFair = new ReentrantLock2(false);
        
        //先测试5个线程的公平所

       
        testLock(lockFair);

        TimeUnit.SECONDS.sleep(5);
        //再测试5个线程的非公平锁
        testLock(notFair);
        
        //主线程等待子线程的执行结果
        TimeUnit.SECONDS.sleep(5);

        /**
         * 打印结果如下：
         * fair lock, Lock by [0],Waiting by [1]
         fair lock, Lock by [1],Waiting by [3,2,4,0]
         fair lock, Lock by [3],Waiting by [2,4,0,1]
         fair lock, Lock by [2],Waiting by [4,0,1,3]
         fair lock, Lock by [4],Waiting by [0,1,3,2]
         fair lock, Lock by [0],Waiting by [1,3,2,4]
         fair lock, Lock by [1],Waiting by [3,2,4]
         fair lock, Lock by [3],Waiting by [2,4]
         fair lock, Lock by [2],Waiting by [4]
         fair lock, Lock by [4],Waiting by []
         not fair lock, Lock by [0],Waiting by []
         not fair lock, Lock by [0],Waiting by [1,2,3,4]
         not fair lock, Lock by [1],Waiting by [2,3,4]
         not fair lock, Lock by [1],Waiting by [2,3,4]
         not fair lock, Lock by [2],Waiting by [3,4]
         not fair lock, Lock by [2],Waiting by [3,4]
         not fair lock, Lock by [3],Waiting by [4]
         not fair lock, Lock by [3],Waiting by [4]
         not fair lock, Lock by [4],Waiting by []
         not fair lock, Lock by [4],Waiting by []
         */
        
        /*
        * 解释如下：
         * 公平锁为了保证所得获取按照FIFO的原则，而代价是进行了大梁的线程切换
         * 非公平锁虽然可能造成线程饥饿，但是极少的线程切换，保证了更大的吞吐量
        * */
    }
    
    
    
    
}


