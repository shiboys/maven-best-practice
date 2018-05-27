package com.swj.ics.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swj on 2018/4/26.
 * 新生代堆内存配置
 * -Xms:初始堆大小
 * -Xmx:最大堆大小
 * -Xmn:可设置新生代堆内存的大小 新生代一般会设置为整个堆空间的1/3-1/4.也即是新生代 1 ： 老年代为2 为 1：2
 * -XX:SurvivorRatio:用来设置新生代中eden空间和from/to空间的比例。含义：-XX:SurvivorRatio = eden/from = eden/to
 * -XX:SurvivorRatio=2 表示eden为2，from为2，to 为2
 * 总结：不同的堆分布情况，对系统执行产生一定的影响，在实际工作中，应该根据系统的特点做出合理的配置。基本策略：
 * 尽可能的将对象预留在新生代中，减少老年代的GC次数。除了可以设置新生代的绝对大小(-Xmn),还可以使用(-XX:NewRatio)
 * 设置新生代和老年代(tenured generation)的比例，-XX:NewRatio=老年代/新生代 （一般对象经过16次GC之后，进入老年代）
 * 
 */
public class EdenTest {
    
    private static int _1M = 1 * 1024 * 1024;
    
    public static void main(String[] args) {
        
        //第一次配置 GC 的次数很多。就会造成系统负荷增大。
        //-Xms2M -Xmx20M -Xmn2M -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
        
        //第二次配置
        //-Xms20m -Xmx20m -Xmn7m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC

        //第三次配置
        //-XX:NewRatio=老年代/新生代
        //-Xms20m -Xmx20m -XX:NewRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
        
   /*     byte[] b = null;
        //连续向系统申请10M内存
        for(int i = 0;i < 10;i++) {
            b = new byte[1 * 1024 * 1024];
        }*/
       // test2();
        //test_eden();

        //test_turnedGeneration();

        test_old_generation2();
    }
    
    //-Xms20m -Xmx20m -Xmn10m -XX:PermSize=10m -XX:MaxPermSize=10m -XX:+PrintGCDetails
    
    static void test2() {
        byte[] s0 = new byte[2 * _1M];
        byte[] s1 = new byte[2 * _1M];
        byte[] s2 = new byte[2 * _1M];
        byte[] s3 = new byte[2 * _1M];
        byte[] s4 = new byte[2 * _1M];
        // 以上总共增加10M，触发minor GC, 通过GC不难发现，这10M的内存有eden和tenured分摊
        // 此时，新增一个1M的内存 , 再次触发minor GC
        byte[] s5 = new byte[1 * _1M];
        // 新增一个1M的内存，由eden去进行担保
        // 此时再新增一个7M的对象,将导致OOM的发生，Eden区无法存储。eden仍然分担3M的内容，而剩下的15M老年代担保失败，触发Full GC，GC后仍然无法存储，发生OOM
        byte[] s6 = new byte[7 * _1M];
    }
    /*
    * 2015-05-26T14:45:37.987-02001:151.1262:[GC3(Allocation Failure4)
151.126: [DefNew5:629119K->69888K6(629120K)7
, 0.0584157 secs]1619346K->1273247K8(2027264K)9,0.0585007 secs10]
[Times: user=0.06 sys=0.00, real=0.06 secs]11

2015-05-26T14:45:37.987-0200 – GC事件(GC event)开始的时间点.
151.126 – GC时间的开始时间,相对于JVM的启动时间,单位是秒(Measured in seconds).
GC – 用来区分(distinguish)是 Minor GC 还是 Full GC 的标志(Flag). 这里的 GC 表明本次发生的是 Minor GC.
Allocation Failure – 引起垃圾回收的原因. 本次GC是因为年轻代中没有任何合适的区域能够存放需要分配的数据结构而触发的.
DefNew – 使用的垃圾收集器的名字. DefNew 这个名字代表的是: 单线程(single-threaded), 采用标记复制(mark-copy)算法的, 使整个JVM暂停运行(stop-the-world)的年轻代(Young generation) 垃圾收集器(garbage collector).
629119K->69888K – 在本次垃圾收集之前和之后的年轻代内存使用情况(Usage).
(629120K) – 年轻代的总的大小(Total size).
1619346K->1273247K – 在本次垃圾收集之前和之后整个堆内存的使用情况(Total used heap).
(2027264K) – 总的可用的堆内存(Total available heap).
0.0585007 secs – GC事件的持续时间(Duration),单位是秒.
[Times: user=0.06 sys=0.00, real=0.06 secs] – GC事件的持续时间,通过多种分类来进行衡量: 
user – 此次垃圾回收, 垃圾收集线程消耗的所有CPU时间(Total CPU time).
sys – 操作系统调用(OS call) 以及等待系统事件的时间(waiting for system event)
real – 应用程序暂停的时间(Clock time). 由于串行垃圾收集器(Serial Garbage Collector)只会使用单个线程, 所以 real time 等于 user 以及 system time 的总和.
    * */
    
    static void test_eden() {
        //初始化的对象在eden区
        //参数：-Xmx64M -Xms64M -XX:+PrintGCDetails
        for(int i = 0;i < 5; i++) {
            byte[] b = new byte[1024*1024];
        }
    }
    
    //测试进入老年代对象
    static void test_turnedGeneration() {
       //初始哈参数：-Xmx1024M -Xms1024M -XX:MaxTenuringThreshold=15 -XX:PrintGCDetails
        //-XX:PrintHeadAtGC
        //不会用到老年代
        /*
        * -Xmx10M
-Xms10M
-XX:+UseSerialGC
-XX:MaxTenuringThreshold=1
-XX:+PrintGCDetails
-XX:+PrintHeapAtGC
        * */
        Map<Integer,byte[]> map = new HashMap<>();
        for(int i = 0; i < 5 ;i++) {
            byte[] b = new byte[1024*1024];
            map.put(i,b);
        }
    }

    /**
     * -Xmx1024M
     -Xms1024M
     -XX:+UseSerialGC
     -XX:MaxTenuringThreshold=15
     -XX:+PrintGCDetails
     -XX:+PrintHeapAtGC
     年岁有加，并非垂老，理想丢弃，方坠暮年！
     */
    
    static void test_old_generation2() {
        for(int i = 0;i < 20 ; i++) {
            for(int j = 0;j < 300;j++) {
                byte[] b = new byte[1024*1024];
            }
        }
    }



}
