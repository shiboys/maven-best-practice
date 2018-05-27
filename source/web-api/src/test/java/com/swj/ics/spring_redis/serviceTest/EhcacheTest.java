package com.swj.ics.spring_redis.serviceTest;

import com.swj.ics.spring_redis.service.IEhcacheService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2016/12/2.
 */
public class EhcacheTest extends SpringTestCase {
    @Autowired
    private IEhcacheService ehcacheService;

    @Test
    public void TestEhCache() throws InterruptedException {
        String para="ehcache";
        String timestamp=ehcacheService.getTimestamp(para);
        System.out.println("currentTimestamp:"+timestamp);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("after 2 seconds:"+ehcacheService.getTimestamp(para));
        TimeUnit.SECONDS.sleep(11);
        System.out.println("after 11 seconds:"+ehcacheService.getTimestamp(para));
    }
}
