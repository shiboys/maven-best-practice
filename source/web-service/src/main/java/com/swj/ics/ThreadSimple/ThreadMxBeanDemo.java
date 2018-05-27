package com.swj.ics.ThreadSimple;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Created by swj on 2017/12/28.
 */
public class ThreadMxBeanDemo {
    public static void main(String[] args) {
        //获取java线程 管理Bean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        //不需要获取同步的monitor和synchronizer信息，仅获取线程和堆栈的信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false,false);
        for(ThreadInfo info : threadInfos) {
            System.out.println("[" + info.getThreadId()+"] " + info.getThreadName());
        }
        //打印结果如下
        /**
         * [11] Monitor Ctrl-Break
         [5] Attach Listener 
         [4] Signal Dispatcher //发送消息个JVM信号的线程
         [3] Finalizer //调用对象finalize方法的线程
         [2] Reference Handler //清除Reference 的线程
         [1] main //main线程 用户程序入口
         */
    }
}
