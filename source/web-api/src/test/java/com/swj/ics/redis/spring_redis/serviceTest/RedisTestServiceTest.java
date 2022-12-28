package com.swj.ics.redis.spring_redis.serviceTest;

import com.swj.ics.redis.spring_redis.service.IRedisTestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by swj on 2016/11/30.
 * 这个单元测试好坑爹，其实还是Maven没有配置好，香瓜你的依赖没有加好
 */
public class RedisTestServiceTest extends SpringTestCase {
    @Autowired
    private IRedisTestService redisTestService;

   // @Test
    public void test_method_getTimestamps() throws InterruptedException {
        String para="para";
        String timeStamps= redisTestService.getTimestamps(para);
        System.out.println("第一次调用："+timeStamps);
        Thread.sleep(2000);
        System.out.println("2秒之后调用:"+redisTestService.getTimestamps(para));
        System.out.println("有效期为10秒");
        Thread.sleep(11000);
        System.out.println("11秒之后调用："+redisTestService.getTimestamps(para));
    }


}
