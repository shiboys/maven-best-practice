package com.swj.ics.multiThread_Concurrent.ThreadPoolExecutorDemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.omg.SendingContext.RunTime;

/**
 * author shiweijie
 * date 2018/10/8 下午3:43
 */
public class FIleFolderTest {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static long getTargetFolderSize(File sourceFile)  {
        if(sourceFile == null || !sourceFile.exists()) {
            return 0L;
        }
        if(sourceFile.isFile()) {
            return sourceFile.length();
        }
        long size = 0;
        File[] files = sourceFile.listFiles();
        List<File> subFolders = new ArrayList<>();
        for(File f : files) {
            if(f.isFile()) {
                size += f.length();
            } else if(f.isDirectory()) {
                subFolders.add(f);
            }
        }
        for(File folder: subFolders) {
            FutureTask<Long> call = ( FutureTask<Long>)  threadPool.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return getTargetFolderSize(folder);
                }
            });
            try {
               size += call.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdownNow();
        return size;
    }

    public static void main(String[] args) {
        String filePath = "/Users/shiweijie/Desktop/share";
        File file = new File(filePath);
        long size = getTargetFolderSize(file);
        System.out.println("size is " +size);
    }
}
