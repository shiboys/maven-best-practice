package com.swj.ics.multiThread_Concurrent.ThreadPoolExecutorDemo;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by swj on 2018/2/4.
 */
public class MyRejected implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("自定义处理。。。");
        System.out.println("当前被拒绝的任务信息为："+r.toString());
    }
}
