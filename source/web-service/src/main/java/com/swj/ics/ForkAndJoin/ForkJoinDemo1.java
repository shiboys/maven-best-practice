package com.swj.ics.ForkAndJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by swj on 2017/12/26.
 */
public class ForkJoinDemo1 {
    //ForkJoin框架的执行必须在ForkJoinPool里面执行
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //生成一个任务，负责计算1..100
        CountTask countTask = new CountTask(1,100);
        ForkJoinTask<Integer> forkResult = forkJoinPool.submit(countTask);
        try {
            System.out.println("forkResult is " + forkResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
