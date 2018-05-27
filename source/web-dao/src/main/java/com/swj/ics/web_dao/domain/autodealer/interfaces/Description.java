package com.swj.ics.web_dao.domain.autodealer.interfaces;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 成员类型是受限制的，合法的类型包括基本的类型以及String,Class,Annotation,Enumeration
 * 如果注解只有一个成员，那么这个成员取名必须叫做value(),使用的时候可以忽略成员名和=号
 * 注解类可以没有成员，没有成员的注解类成为标识注解
 * Created by swj on 2017/5/21.
 * @interface注解的不是一个接口，这是使用@interface关键字定义的一个注解。
 *@Target,@Retention 等4个注解被称为元注解。
 */
@Target({ElementType.METHOD,ElementType.TYPE}) //@Target 标识注解的作用域，Type表示 类接口。
@Retention(RetentionPolicy.RUNTIME)//声明周期，runtime表示运行时存在，可以通过反射读取。
@Inherited //标识性注解，表示它允许子注解继承它
@Documented //生成javadoc的时候会包含注解
public @interface Description {
    String desc();//类似于接口里面的方法，但是是一个成员变量。
    String author();
    int age() default 18;//成员变量可以使用default指定一个默认值
}
