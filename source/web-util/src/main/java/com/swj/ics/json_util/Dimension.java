package com.swj.ics.json_util;

import java.lang.annotation.*;

/**
 * Created by swj on 2017/2/8.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME) //驻留形式
@Target(ElementType.FIELD)
public @interface Dimension {
    String valueType();
}

