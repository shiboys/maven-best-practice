package com.swj.ics.web_dao.domain.autodealer.interfaces;

import java.lang.annotation.*;

/**
 * Created by swj on 2017/5/21.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)//CLASS编译时记录到class，运行时忽略。Source,只在源码中显示，编译时丢弃.Runtime:运行时存在，可以通过反射读取。是
@Inherited
@Documented
public @interface Description2 {
    String value();
}
