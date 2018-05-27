package com.swj.ics.redislockframework;

import java.lang.annotation.*;

/**
 * Created by swj on 2017/11/20.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface LockedComplexObject {
    String field() default "";//当使用该注解的参数的类型为复杂类型的时候，
    //可以给该使用 field 标注的类型的一个字段/成员变量 上加一个锁。比如一个商品对象的ID
}
