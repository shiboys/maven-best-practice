package com.swj.ics.jvm;

/**
 * author shiweijie
 * date 2018/6/6 下午8:29
 * 并不是虚拟机运行时的一部分，也不是虚拟机规范所规范！
 * XX:MaxPermSize=256m -XX:NewSize=256m -XX:MaxNewSize=512m -XX:MaxDirectMemorySize=128M。
 * 设置持久代最大值 MaxPermSize:256m
 * 设置年轻代大小 NewSize:256m
 年轻代最大值 MaxNewSize:512M
 最大堆外内存（直接内存）MaxDirectMemorySize：128M

 -Xms512m -Xmx768m -XX:MaxPermSize=256m -XX:NewSize=256m -XX:MaxNewSize=512m
 */
public class DirectMemory {

}
