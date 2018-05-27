package com.swj.ics.redislock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by swj on 2017/11/26.
 */
public class SecKillImpl implements SecKillInterface {
    
    static Map<Long,Long> inventory ;
    static  {
        inventory = new ConcurrentHashMap<>();
        //ID为1001和1002的各自初始化为10000数量
        inventory.put(1001L,10000L);
        inventory.put(1002L,10000L);
    }
    
    @Override
    public void secKill(String args, Long args2) {
        reduceInventory(args2);
    }

    /**
     * 模拟商品秒杀
     * @param commodityId
     * @return
     */
    protected Long reduceInventory(Long commodityId) {
         if (!inventory.containsKey(commodityId)) {
             return -1L;
         }
         long currAmount = inventory.get(commodityId);
        inventory.put(commodityId,currAmount - 1);
        return inventory.get(commodityId);
    }
}
