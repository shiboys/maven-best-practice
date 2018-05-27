package com.swj.ics.jvm;

/**
 * Created by swj on 2018/5/1.
 * Java的垃圾收集器：
 * Serial
 * ParNew
 * Parallel Scanvage:采用了复制算法收集器，也是多线程独占形式手机器，非常关注系统吞吐量 可以打开自适应模式。
 * -XX:MaxGCPauseMills 设置最大垃圾收集停顿时间。
 * ParallelOldGC 和新生代的ParallelGC一样，也是一种关注吞吐量的回收器，他使用了标记压缩算法进行实现。
 * CMS：Concurrent Mark Sweep  CMS收集器在回收的过程中，应用程序仍然在不停的工作。CMS在某一个阈值的时候开始回收
 * -XX:CMSInitiatingOccupacyFraction来指定。默认为68，也就是当老年代的空间使用率达到68%的时候，会执行CMS。 
 * Serial New收集器是针对新生代的收集器，采用的是复制算法
 Parallel New（并行）收集器，新生代采用复制算法，老年代采用标记整理
 Parallel  Scavenge（并行）收集器，针对新生代，采用复制收集算法
 Serial Old（串行）收集器，新生代采用复制，老年代采用标记清理
 Parallel   Old（并行）收集器，针对老年代，标记整理
 CMS收集器，基于标记清理
 G1收集器：整体上是基于标记清理，局部采用复制
 * 
 */
public class GCDemo {
}
