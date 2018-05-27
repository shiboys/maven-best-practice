package com.swj.ics.jvm;

/**
 * Created by swj on 2018/4/26.
 * Java虚拟机提供了参数-Xss来指定线程的最大栈空间，整个参数决定了函数可以调用的最大深度。
 */
public class VMStackTest {
    
    static int counter = 0;
    static void recursiveInfinitLoop() {
        counter ++;
        recursiveInfinitLoop();
    }
    public static void main(String[] args)  {
        //线使用虚拟机参数 -Xss1M,然后再使用-Xss5M
        try {
            recursiveInfinitLoop();
        } catch (Exception e) {
            System.out.println("最大调用深度："+counter);
            e.printStackTrace();
        } finally {
            System.out.println("最大调用深度："+counter);
        }
    }
    
}
