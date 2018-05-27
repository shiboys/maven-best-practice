package com.swj.ics.jvm;

/**
 * Created by swj on 2018/4/24.
 */
public class JVM_HeapTest {
    //查看GC信息
    // -Xms5M -Xmx20M -XX:+PrintGCDetails -XX:+UseSerialGC -XX:+PrintCommandLineFlags
    
    //-XX:PrintGC 启用这个参数之后，虚拟机启动起来，只要遇到GC就会打印日志。
    // UseSerialGC 配置串行垃圾回收器
    // PrintGCDetails 可以查看详细信息，包括各个区的情况
    //-Xms 设置Java程序启动时初始堆的大小
    //-Xmx 设置Java程序能获得的最大堆大小。
    // PrintCommandLineFlags 可以将我们上面配置的参数显式的打印出来
    //在实际工作中，我们可以直接将初始堆大小与最大堆大小设置为相同的值，这样的好处是可以减少
    //程序运行时的垃圾回收次数，从而提高性能。

    /** 由于JDK1.8 取消了PermGeneration ，取而代之的是MetaSpace
     * 元空间的本质和永久代类似，都是对JVM规范中方法区的实现。不过元空间与永久代之间最大的区别在于：元空间并不在虚拟机中，
     * 而是使用本地内存。因此，默认情况下，元空间的大小仅受本地内存限制，但可以通过以下参数来指定元空间的大小：
     * def new generation   total 1920K, used 60K [0x00000000fec00000, 0x00000000fee10000, 0x00000000ff2a0000)
     eden space 1728K,   3% used [0x00000000fec00000, 0x00000000fec0f118, 0x00000000fedb0000)
     from space 192K,   0% used [0x00000000fedb0000, 0x00000000fedb0000, 0x00000000fede0000)
     to   space 192K,   0% used [0x00000000fede0000, 0x00000000fede0000, 0x00000000fee10000)
     tenured generation   total 8196K, used 5990K [0x00000000ff2a0000, 0x00000000ffaa1000, 0x0000000100000000)
     the space 8196K,  73% used [0x00000000ff2a0000, 0x00000000ff879a00, 0x00000000ff879a00, 0x00000000ffaa1000)
     Metaspace       used 3050K, capacity 4494K, committed 4864K, reserved 1056768K
     class space    used 327K, capacity 386K, committed 512K, reserved 1048576K
     
     * @param args
     */
    
    public static void main(String[] args) {
        System.out.println("max memory：" + Runtime.getRuntime().maxMemory());
        System.out.println("free memory:" + Runtime.getRuntime().freeMemory());
        System.out.println("total memory:" + Runtime.getRuntime().totalMemory());
        
        byte[] b1 = new byte[1*1024*1024];
        System.out.println("分配了1M内存");
        System.out.println("max memory：" + Runtime.getRuntime().maxMemory());
        System.out.println("free memory:" + Runtime.getRuntime().freeMemory());
        System.out.println("total memory:" + Runtime.getRuntime().totalMemory());

        byte[] b2 = new byte[4*1024*1024];
        System.out.println("分配了4M内存");
        System.out.println("max memory：" + Runtime.getRuntime().maxMemory());
        System.out.println("free memory:" + Runtime.getRuntime().freeMemory());
        System.out.println("total memory:" + Runtime.getRuntime().totalMemory());
        
        int a = 0x00000000fec00000;
        int b =0x00000000fedb0000 ;
        System.out.println("edan区大小为：" + ( (b - a) / 1024));
    }
}
