package com.swj.ics.redislock;

import com.swj.ics.redislockframework.CacheLock;

/**
 * Created by swj on 2017/11/26.
 */
public interface SecKillInterface {
    @CacheLock(lockedPrefix = "Test_Prefix")
    void secKill(String args,Long args2);
}
