package com.swj.ics.spring_redis.service.impl;

import com.swj.ics.spring_redis.service.IEhcacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by swj on 2016/12/2.
 *Ehcache是一个纯Java的进程内缓存框架，具有快速，精炼等特点。是hibernate中默认的cache provider.
 * Ehcache是一种广泛使用的开源的Java分布式缓存，主要面向通用缓存，JavaEE和磁盘存储。
 * Ehcache具有内存和磁盘存储，缓存加载器，缓存扩展，缓存异常处理，一个gzip缓存servlet过滤器，支持Rest和Soap api等
 * 优点：
 * 1、快速
 * 2、简单
 * 3、多种缓存策略
 * 4、缓存数据有2级，内存和磁盘
 * 5、缓存数据会在虚拟机重启的过程中写入磁盘
 * 6、可以通过RMI，可插入API等方式进行分布式缓存。
 * 7、。。。。
 * 缺点：
 * 使用Cache时候，非常占用磁盘空间，因为DiskCache的算法简单，只是对元素直接追加存储，因此搜索元素的时候非常快
 * 。如果使用DiskCache,在频繁的应用中，磁盘很快就会满。
 * 2、不能保证数据的安全
 *
 * 使用spring aop 对数据进行整合，可以灵活的对方法返回的结果进行缓存。
 */
@Service
public class EhcacheServiceImpl implements IEhcacheService {

    //这里注解中的 value="testCache"跟ehcache-setting.xml中的一样
    @Cacheable(value = "cacheTest",key ="#param" )
    public String getTimestamp( String param )
    {
        Long  currentTimeMills= System.currentTimeMillis();
        return currentTimeMills.toString() ;
    }
}
