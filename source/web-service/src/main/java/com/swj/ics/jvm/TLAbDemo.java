package com.swj.ics.jvm;

/**
 * Created by swj on 2018/4/29.
 * TLAB的全称是Thread Local Allocate Buffer.也即是线程本地分配缓存，从名字上看是一个线程专用的内存分配区域。
 * 是为了加速对象分配而生的。每一个线程都会产生一个TLAB区域，该线程独享的工作区域，Java虚拟机使用这种TLAB区来
 * 避免多线程冲突的问题，提高了对象分配的效率。TLAB一般不会太大，当大对象无法再TLAB 上分配的时候，则会直接分配在堆上。
 * -XX:+UseTLAB
 * -XX:TLABSize 
 */

import java.util.HashMap;
import java.util.Map;

/**
 * 另外，对于新生代中比较大的连续的对象，新生代Eden区域无法装入的情况下，Jvm里面有个参数
 * -XX:PretenureSizeThreshold可以指定进入老年代的对象大小。但是要注意TLAB区域优先分配空间。
 */
public class TLAbDemo {
    public static void main(String[] args) {
        //test_PretenureSizeThreshold();
        test_tlab();
    }
    static void test_PretenureSizeThreshold(){
        //-Xmx30M -Xms30M -XX:+UseSerialGC -XX:PrintGCDetails -XX:PretenureSizeThreshold=1024000;
        //10241000 < 1024*1024 ，也即小于1K
        Map<Integer,byte[]> map = new HashMap<>();
        for(int i=0,len = 5; i < len; i++) {
            byte[] b = new byte[1024*1024];
            map.put(i,b);//1M 
        }
        //运行结果如下：
        /**
         * Heap
         def new generation   total 9216K, used 2497K [0x00000000fe200000, 0x00000000fec00000, 0x00000000fec00000)
         eden space 8192K,  30% used [0x00000000fe200000, 0x00000000fe470730, 0x00000000fea00000)
         from space 1024K,   0% used [0x00000000fea00000, 0x00000000fea00000, 0x00000000feb00000)
         to   space 1024K,   0% used [0x00000000feb00000, 0x00000000feb00000, 0x00000000fec00000)
         tenured generation   total 20480K, used 5120K [0x00000000fec00000, 0x0000000100000000, 0x0000000100000000)
         the space 20480K,  25% used [0x00000000fec00000, 0x00000000ff100050, 0x00000000ff100200, 0x0000000100000000)
         Metaspace       used 3083K, capacity 4494K, committed 4864K, reserved 1056768K
         class space    used 331K, capacity 386K, committed 512K, reserved 1048576K
         */
    }
    
    static void test_tlab() {
        //-Xmx30M -Xms30M -XX:+UseSerialGC -XX:PrintGCDetails -XX:PretenureSizeThreshold=1000;
        
        Map<Integer,byte[]> map = new HashMap<>();
        for(int i=0,len = 5 * 1024; i < len; i++) {
            byte[] b = new byte[1024];
            map.put(i,b);//1M 
        }
        //运行结果如下：
        /**
         * Heap
         def new generation   total 9216K, used 7902K [0x00000000fe200000, 0x00000000fec00000, 0x00000000fec00000)
         eden space 8192K,  96% used [0x00000000fe200000, 0x00000000fe9b7a70, 0x00000000fea00000)
         from space 1024K,   0% used [0x00000000fea00000, 0x00000000fea00000, 0x00000000feb00000)
         to   space 1024K,   0% used [0x00000000feb00000, 0x00000000feb00000, 0x00000000fec00000)
         tenured generation   total 20480K, used 35K [0x00000000fec00000, 0x0000000100000000, 0x0000000100000000)
         the space 20480K,   0% used [0x00000000fec00000, 0x00000000fec08d28, 0x00000000fec08e00, 0x0000000100000000)
         Metaspace       used 3150K, capacity 4494K, committed 4864K, reserved 1056768K
         class space    used 338K, capacity 386K, committed 512K, reserved 1048576K
         
         解释如下：虚拟机对于体积不大的对象，会优先把数据分配的tlab区，因此就失去了在老年代中分配的机会。
         */
        
    }
}
