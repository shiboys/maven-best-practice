package com.swj.ics.redis.spring_redis.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by swj on 2016/12/2.
 */
//告诉spring，其上下文的位置。
@ContextConfiguration(locations = {"classpath*:spring-ehcache.xml"})
//使用标准的Junit @RunWith注解来告诉JUnit来使用Spring Test Runner
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringServiceTestBase extends AbstractJUnit4SpringContextTests {

}
