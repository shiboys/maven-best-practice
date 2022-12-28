package com.swj.ics.redis.spring_redis.service.impl;

import com.swj.ics.redis.spring_redis.service.IRedisTestService;
import org.springframework.stereotype.Service;

/**
 * Created by swj on 2016/11/29.
 */
    @Service
    public class RedisTestServiceImpl implements IRedisTestService {

    public String getTimestamps(String para) {
       Long timestamp=System.currentTimeMillis();
        return timestamp.toString();
    }
}
