package com.swj.ics.redis.spring_redis.serviceTest;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by swj on 2016/11/30.
 * 单元测试辅助类
 */
//指定备bean注入的配置文件 "classpath:spring-redis.xml",
@ContextConfiguration(locations = {"classpath:spring-transaction.xml", "classpath:spring-ehcache.xml"})
//使用标准的JUnit的 @RunWith来告诉JUnit使用Spring TestRunner
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringTestCase  extends AbstractJUnit4SpringContextTests{

}
