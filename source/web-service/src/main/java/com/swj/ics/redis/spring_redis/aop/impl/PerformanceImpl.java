package com.swj.ics.redis.spring_redis.aop.impl;

import com.swj.ics.redis.spring_redis.aop.IPerformance;

/**
 * Created by swj on 2017/1/5.
 */
public class PerformanceImpl implements IPerformance {
    @Override
    public void perform() {
        System.out.println("perform implement");
    }
}
