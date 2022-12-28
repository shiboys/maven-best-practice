package com.swj.ics.redis.redislockframework;

import java.lang.annotation.*;

/**
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
    
}
