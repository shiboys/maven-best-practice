package com.swj.ics.juc;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2022/06/12 16:39
 * ConcurrentSkipListMap 是基于 treemap 的设计原理来实现的，略有不同的是，skipList 是基于跳表，treeMap
 * 是基于红黑树实现的，ConcurrentSkipListMap 的特点是存取平均时间复杂度是O(log(n)),适用于大数据量存取的场景
 * 最常见的是基于跳表实现的数据量比较大的缓存
 * ConcurrentHashMap vs ConcurrentSkipListMap
 * chm 在数据量比较大的时候，链表会转化为红黑树。红黑树在并发的情况下，插入和删除有个平衡的过程，会牵涉到大量的节点
 * 因此竞争锁资源的带价比较高。
 * 跳表的操作是针对局部，需要锁住的节点比较少，在病发场景下，性能回更好一些。在非线程安全的Map 容器中，基于红黑树实现的
 * TreeMap 在单线程的性能表现的并不比跳跃表差
 *
 * 因此实现了 在非线程安全的 Map 容器中，使用 treeMap 容器来存取大数据，在线程安全的Map 容器中，用 skipListMap 容器来
 * 存取大数据。
 */
public class SkipListCase {
  public static void main(String[] args) {
    ConcurrentSkipListMap<String, Integer> skipListMap = new ConcurrentSkipListMap();
    for (int i = 0; i < 10; i++) {
      skipListMap.put("number:" + i, i);
    }
    for (Map.Entry<String, Integer> entry : skipListMap.entrySet()) {
      System.out.println(entry.getKey());
    }
  }
}
