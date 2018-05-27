package com.swj.ics.ThreadSimple.CustomeThreadPoolDemo;

import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/13.
 */
public class HttpServerMainClass {
    public static void main(String[] args) throws InterruptedException {
        /*
        * 1、设置SimpleHttpServer的BasePath
        * 2、调用其run方法。
        * 3、开启浏览器访问
        * */
        //showSimpleHttpServer();
        
        /*
        * 测试线程池2
        * 使用10个线程 来计算 1-1000之间数值之和
        * 或者去网上下载10张图片
        * */
        
        ThreadPool<CalculateSumRunnable> threadPool = new DefaultThreadPool<>(10);
        
        for (int i = 1 ;i <= 10 ; i++) {
            threadPool.execute(new CalculateSumRunnable((i-1) * 1000 +1,i*1000));
        }

        TimeUnit.SECONDS.sleep(5);
        System.out.println("down");
        
    }
    
    static class CalculateSumRunnable implements Runnable {
        
        private int minNum;
        private int maxNum;
        
        public CalculateSumRunnable(int min,int max) {
            this.minNum = min;
            this.maxNum = max;
        }
        
        @Override
        public void run() {
            System.out.println(String.format("min:%d,max:%d",minNum,maxNum));
            long sum = 0;
            for (int i = minNum;i <= maxNum;i ++ ) {
                sum += i;
            }
            System.out.println( Thread.currentThread().getName() +",the sum is " +sum);
        }
    }

    private static void showSimpleHttpServer() {
        String basePath= "D:\\simpleHttpServer";
        SimpleHttpServer.setBasePath(basePath);
        try {
            SimpleHttpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
