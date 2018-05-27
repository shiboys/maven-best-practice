package com.swj.ics.redislockframework;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by swj on 2017/11/20.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) //驻留方式，运行时驻留
@Documented
public @interface CacheLock {
    String lockedPrefix() default "";
    long timeOut() default 2000;//锁定对象的时间 2秒
    int expireTime() default 10000;//key 在redis里面存在的时间。10秒钟。
}
