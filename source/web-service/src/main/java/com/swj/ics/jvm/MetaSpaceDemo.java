package com.swj.ics.jvm;

/**
 * Created by swj on 2018/4/26.
 *  Java虚拟机规范只是规定了有方法区这么个概念和它的作用，并没有规定如何去实现它，在其他JVM上也不存在永久代
 *  1、新生代 Eden + From Survivor + To Survivor
 *  2、Old Generation
 *  3、永久代（方法区的实现）:PermGen ———>替换为MetaSpace(本地内存)
 *  为什么要废弃 PermGen???
 *  This is part of the JRockit and Hotspot convergence effort. 
 *  JRockit customers do not need to configure the permanent generation 
 *  (since JRockit does not have a permanent generation) and are accustomed to not configuring the permanent generation.
 *  1、移除永久代是为融合HotSpot JVM与 JRockit VM而做出的努力，因为JRockit没有永久代，不需要配置永久代
 *  2、现实中使用的问题
 *  由于永久代内存经常不够用或者发生内存泄露，经常爆出java.lang.OutOfMemoryError:PermGen
 *  
 *  三、深入理解元空间（Metaspace）
 3.1元空间的内存大小
 元空间是方法区的在HotSpot jvm 中的实现，方法区主要用于存储类的信息、常量池、方法数据、方法代码等。
 方法区逻辑上属于堆的一部分，但是为了与堆进行区分，通常又叫“非堆”。
 
 Java8就彻底的移除了堆的永久区，取而代之的是元空间（MetaSpace），它最大的特点就是存储在物理内存（本地内存），
 这样的话减少了方法区进行垃圾回收的概率。一般情况下，是不会出现OOM的
 相应的jvm调用参数 PermGenSize  MaxPermGenSize都被剔除，取而代之的是MetaSpaceSize和MaxMetaSpaceSize
 */
public class MetaSpaceDemo {
    
}
