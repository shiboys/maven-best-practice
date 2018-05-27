package com.swj.ics.ForkAndJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by swj on 2017/12/26.
 */
public class CountTask extends RecursiveTask<Integer> {
    
    private static final int THRESHOLD = 2;
    
    private int start;
    private int end;
    
    public CountTask(int start,int end) {
        this.start = start;
        this.end = end;
    }
    
    
    @Override
    protected Integer compute() {
        int diff = end - start;
        int sum = 0;
        if (diff <= THRESHOLD) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middleValue = (start + end) / 2;
            CountTask leftTask = new CountTask(start,middleValue);
            CountTask rightTask = new CountTask(middleValue+1,end);
            //执行子任务
            ForkJoinTask<Integer> leftFork = leftTask.fork();
            ForkJoinTask<Integer> rightFork = rightTask.fork();
            //等待任务完成
            try {
                int leftResult =  leftFork.get();
                int rightResult = rightFork.get();
                sum = leftResult + rightResult;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }
}
