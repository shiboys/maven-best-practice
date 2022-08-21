package com.swj.ics.juc;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2022/06/12 23:09
 * DelayQueue 是一个支持延时获取延迟获取元素的无界阻塞队列
 * DelayQueue 主要用于 2 方面：
 *  1、缓存，清理掉缓存中超时的数据
 *  2、任务超时处理
 * DelayQueue 实现的关键主要有如下几个：
 *  1、可重入锁 ReentrantLock
 *  2、用于阻塞和通知的Condition对象
 *  3、根据 delay 时间排序的优先级队列，PriorityQueue
 *  4、用于优化阻塞通知的线程元素 leader
 *
 *  以支持优先级无界队列的PriorityQueue作为一个容器，容器里面的元素都应该实现Delayed接口，
 *  在每次往优先级队列中添加元素时以元素的过期时间作为排序条件，最先过期的元素放在优先级最高
 */
public class DelayQueueTest {
}
