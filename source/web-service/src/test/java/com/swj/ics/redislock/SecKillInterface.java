package com.swj.ics.redislock;

import com.swj.ics.redislockframework.CacheLock;
import com.swj.ics.redislockframework.LockedObject;

/**
 * Created by swj on 2017/11/26.
 */
public interface SecKillInterface {
    @CacheLock(lockedPrefix = "Test_Redis_SecKill")
    void secKill(String args,@LockedObject Long itemId);
}
