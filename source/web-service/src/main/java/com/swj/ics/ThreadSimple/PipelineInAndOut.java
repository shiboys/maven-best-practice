package com.swj.ics.ThreadSimple;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * Created by swj on 2018/1/7.
 * 管道输入/输出流。
 * 管道输入输出流和普通的文件输入输出流或者网络输入/输出流不同之处在于
 * 它主要用于线程之间的数据传输，而传输的介质为内存。
 */
public class PipelineInAndOut {
    
    public static void main(String[] args) throws IOException {
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader();
        Thread readerThread = new Thread( new Print(pipedReader),"readerThread");
        //这里一定要使输入和输出流进行连接，否则在使用的时候会抛出异常
        pipedWriter.connect(pipedReader);
        readerThread.start();
        int read = 0 ;
        try {
            System.out.println("请输入线程间要传输的内容:");
            while((read = System.in.read()) != -1) {
                pipedWriter.write(read);
            }
        } finally {
            pipedWriter.close();
        }
    }
    
    static class Print implements Runnable {
        private PipedReader pipedReader;
        
        public Print(PipedReader pipedReader) {
            this.pipedReader = pipedReader;
        }
        @Override
        public void run() {
            int receive = 0 ;
            
            try {
                while ((receive = pipedReader.read()) != -1) {
                    System.out.print((char)receive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
* 总结：
* 对于Pipe类型的流，必须要新进行绑定，也就是盗用connect()方法,如果没有将输入和输出流
* 绑定起来，对于该流的访问将抛出异常。
* */
