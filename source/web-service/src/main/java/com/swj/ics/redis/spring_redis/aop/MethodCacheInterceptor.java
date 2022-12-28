package com.swj.ics.redis.spring_redis.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by swj on 2016/11/30.
 *采用 Spring Aop切面方式进行缓存，已经配置在srping-redis.xml里面
 */
    public class MethodCacheInterceptor implements MethodInterceptor {

    private RedisTemplate<Serializable,Object> redisTemplate;

    /**
     * 默认key的过期时间10秒
     */
    private long defaultCacheExpireTime=10L;

    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object value=null;
        String targetName=invocation.getThis().getClass().getName();
        String methodName=invocation.getMethod().getName();

        Object[] arguments= invocation.getArguments();

        String key=getMethodKey(targetName,methodName,arguments);
        //判断是否有缓存
        try {
            if(existsKey(key))
            {
                return getObjectFromRedis(key);
            }
            else
            {
                value =invocation.proceed();
                if(value!=null)
                {
                    final String tkey=key;
                    final Object tvalue=value;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                          setObjectToRedis(tkey,tvalue,defaultCacheExpireTime);
                        }
                    }).start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if(value==null)//有可能getValue from redis就报错了
                invocation.proceed();
        }
        return value;
    }

    private String getMethodKey(String targetName,String methodName,Object[] arguments)
    {
        StringBuffer sb=new StringBuffer(100);
        sb.append(targetName).append("_").append(methodName);
        if(arguments!=null && arguments.length>0)
        {
             Stream.of(arguments).forEach(x->
             {
                 sb.append("_").append(x);
             });
        }
        return sb.toString();
    }

    private  boolean existsKey(String key)
    {
        return redisTemplate.hasKey(key);
    }

    private Object getObjectFromRedis(final String key)
    {
        Object result=null;
        ValueOperations<Serializable,Object> ops= redisTemplate.opsForValue();
        result=ops.get(key);
        return result;
    }

    private boolean setObjectToRedis(final String key,Object value,long expireTime)
    {
        boolean success=false;
        try {
            ValueOperations<Serializable,Object> operations= redisTemplate.opsForValue();
            operations.set(key,value);
            //operations.set(key,value,expireTime, TimeUnit.SECONDS);//保留
            redisTemplate.expire(key,expireTime,TimeUnit.SECONDS);
            success=true;
        } catch (Exception e) {
            e.printStackTrace();
            success=false;
        }
        return success;
    }

}
