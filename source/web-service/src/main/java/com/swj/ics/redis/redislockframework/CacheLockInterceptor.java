package com.swj.ics.redis.redislockframework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CacheLockInterceptor implements InvocationHandler {
    public static int ERROR_COUNT = 1;
    //真正被代理的对象
    private Object proxied;
    
    public CacheLockInterceptor(Object proxied) {
        this.proxied = proxied;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CacheLock cacheLock =  method.getAnnotation(CacheLock.class);
        if (cacheLock == null) { //没有cacheLock注解的方法，pass掉
            System.out.println("no cacheLock annotation");
            return method.invoke(proxied,args);
        }
        //获取方法中的参数注解
        Annotation[][] annotations = method.getParameterAnnotations();
        //根据参数列表和参数注解来获取指定的被锁定的参数
        Object lockObject = getLockedObject(args,annotations);
        String objectValue = String.valueOf(lockObject);
        //这里的参数key一定是普通类型或者普通类型的Box装箱类型，或者可以使复杂类型的
        //field字段指定的普通类型，
        RedisLock redisLock = new RedisLock(cacheLock.lockedPrefix(),objectValue);
       // RedisExpireLock redisLock = new RedisExpireLock(cacheLock.lockedPrefix(),objectValue);
        boolean isLocked = redisLock.lock(cacheLock.timeOut(),cacheLock.expireTime());
        if (!isLocked) { //如果获取锁失败
            ERROR_COUNT += 1;
            System.out.println(" get the lock failed");
        }
        try {
          return  method.invoke(this.proxied,args);
        } finally {
            redisLock.unLock();
        }
        
    }

    private Object getLockedObject(Object[] args, Annotation[][] annotations) throws CacheLockException {
        int argIndex = -1;
        for(int i = 0,len = annotations.length;i < len;i++) {
            for (int j=0,lenj = annotations[i].length; j < lenj;i++) {
                if (annotations[i][j] instanceof LockedObject) {
                    argIndex = i;
                    break;
                } else if (annotations[i][j] instanceof LockedComplexObject) {
                    String fieldName = ((LockedComplexObject)annotations[i][j]).field();
                    try {
                        //todo这里应该是获取属性值
                       return  args[i].getClass().getField(fieldName);
                    } catch (NoSuchFieldException e) {
                        throw new CacheLockException("被注解的参数对象中没有该属性：" + fieldName);
                    }
                }
            }
            
            if (argIndex != -1) {
                break;
            }
        }
        if (argIndex == -1) {
            throw new CacheLockException("请指定被锁定的参数");
        }
        return args[argIndex];
    }
}
