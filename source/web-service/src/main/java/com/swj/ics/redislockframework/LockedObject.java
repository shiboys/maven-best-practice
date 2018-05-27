package com.swj.ics.redislockframework;

import java.lang.annotation.*;

/**
 * Created by swj on 2017/11/20.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
    
}
