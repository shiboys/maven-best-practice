package com.swj.ics.redislockframework;

import java.util.*;
import java.util.stream.Collectors;
import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * Created by swj on 2017/11/20.
 */
public class RedisClient {
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public RedisClient() {
    }

    /**
     * 根据key来获取相应的value
     * @param key key
     * @return value
     */
    public Object getByKey(String key) {
        Jedis client = jedisPool.getResource();
        Object o = null;
        try {
            o = client.get(key);
        } finally {
            jedisPool.returnResource(client);//向連接池归还资源。
        }
        return o;
    }

    /**
     * 判断string类型的key是否存在。
     * @param key
     * @return
     */
    public boolean isKeyExists(String key) {
        Jedis client = jedisPool.getResource();
        boolean isExists = false; 
        try {
            isExists = client.exists(key);
        } finally {
            jedisPool.returnResource(client);
        }
        return isExists;
    }
    
    public boolean set(String key,String value) {
        Jedis client = jedisPool.getResource();
        try {
            String result = client.set(key,value);
            if ("OK".equalsIgnoreCase(result)) {
                return true;
            } 
            return false;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public Long setnx(String key ,String value) {
        Jedis client = jedisPool.getResource();
        try {
           Long longValue = client.setnx(key,value);
           System.out.println("setnx key =" + key + " value=" + value +" result=" + longValue);
           return longValue;
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /**
     * 
     * @param key
     * @param value
     * @param time 过期时间，秒为单位
     * @return
     */
    public boolean setKeyWithExpireTime(String key,String value,int time) {
        Jedis client = jedisPool.getResource();
        //语法为setex(key,seconds,value);
        try {
            String isSuccess = client.setex(key,time,value);
            if ("OK".equalsIgnoreCase(isSuccess)) {
                return true;
            } 
            return false;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public boolean lpush(String key,List<String> list) {
        Jedis client = jedisPool.getResource();

        try {
            /*Transaction tx = client.multi();
            for(String val : list) {
                client.lpush(val);
            }         
            
            tx.exec();*/
            
            //codis 不支持事务，因此推荐使用下面的方法。

            String[] arrayList = list.toArray(new String[list.size()]);

            client.lpush(key,arrayList);
            return true;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    
    public List<String> lrange(String key) {
        Jedis client = jedisPool.getResource();

        try {
            return client.lrange(key,0,-1);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    public List<String> lrange(String key,int start,int end) {
        Jedis client = jedisPool.getResource();

        try {
            return client.lrange(key,start,end);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public boolean setAnObject(String key ,Object o) {
        Jedis client = jedisPool.getResource();
        try {
            String serializedStr = JSON.toJSONString(o);
            client.set(key,serializedStr);
            return true;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public <T> T getSetT(String key, T newValue) {
        Jedis client = jedisPool.getResource();
        T oldObject;
        try {
            String serializedStr = JSON.toJSONString(newValue);
            String oldObjectStr = client.getSet(key,serializedStr);
            oldObject = (T)JSON.parseObject(oldObjectStr,  newValue.getClass());
        } finally {
            jedisPool.returnResource(client);
        }
        return oldObject;
    }
    
    public <T> T getAnObject(String key,Class<T> cls) {
        Jedis client = jedisPool.getResource();
        T t ;
        try {
            String objStr = client.get(key);
            if (StringUtils.isEmpty(objStr)) {
                return null;
            }
            t = (T)JSON.parseObject(objStr,cls);
        } finally {
            jedisPool.returnResource(client);
        }
        return t;
    }
    
    public List<String> getKeys(String pattern) {
        Jedis client = jedisPool.getResource();
        List<String> keys = new ArrayList<>();
        try {
            Set<String> keySets = client.keys(pattern);
            keys.addAll(keySets);
        } finally {
            jedisPool.returnResource(client);
        }
        return keys;
    }
    
    public boolean delKey(String key) {
        Jedis client =  jedisPool.getResource();
        try {
            System.out.println("del key : "+key);
            client.del(key);
            return true;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public <T> boolean hset(String key ,String field,T newObject) {
        Jedis client = jedisPool.getResource();
        try {
            String serializedVal = JSON.toJSONString(newObject);
            client.hset(key,field,serializedVal);
            return true;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public <T,S> boolean hmset(String key,Map<T,S> map) {
        Jedis client = jedisPool.getResource();
        try {
            Iterator<Map.Entry<T,S>> iterator = map.entrySet().iterator();
            Map<String,String> newMap = new HashMap<>();
            while (iterator.hasNext()) {
                Map.Entry<T,S> entry = iterator.next();
                String ite_key = String.valueOf(entry.getKey());
                String ite_val = JSON.toJSONString(entry.getValue());
                newMap.put(ite_key,ite_val);
            }
            client.hmset(key,newMap);
            return true;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public <T> T hgetObject(String key,String field,Class<T> cls) {
        String fieldValue = hgetString(key,field);
        T t = (T)JSON.parseObject(field,cls);
        return t;
    }

    public String hgetString(String key,String field) {
        Jedis client = jedisPool.getResource();
        try {
            String fieldValue = client.hget(key,field);
            return fieldValue;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public Map<String,String> hgetAll(String key) {
        Jedis client = jedisPool.getResource();
        try {
            return client.hgetAll(key);
        } finally {
            jedisPool.returnResource(client);
        }
    } 
    
    public List<String> hKeys(String key) {
        Jedis client = jedisPool.getResource();
        try {
            Set<String> allKeys = client.hkeys(key);
            List<String> keysList = new ArrayList<>();
            keysList.addAll(allKeys);
            return keysList;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public List<String> hValues(String key) {
        Jedis client = jedisPool.getResource();
        try {
            List<String> values = client.hvals(key);
            return values;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public boolean hExists(String key,String field) {
        Jedis client = jedisPool.getResource();
        try {
            Boolean exists = client.hexists(key,field);
            return exists;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public long incr(String key ) {
        Jedis client = jedisPool.getResource();
        try {
            long result = client.incr(key);
            return result;
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public void hdel(String key,String ... field) {
        Jedis client = jedisPool.getResource();
        try {
            client.hdel(key,field);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public void lpush(String key,String fieldValue) {
        Jedis client = jedisPool.getResource();
        try {
            client.lpush(key,fieldValue);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public void lpush(String key,Object obj){
        Jedis client = jedisPool.getResource();
        try {
            String serializedValue = JSON.toJSONString(obj);
            client.lpush(key,serializedValue);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /**
     * 如果 hash表中的key 大于1000，则不再进行lpush
     * @param key
     * @param field
     */
    public void lpushForErrorMsg(String key,String field) {
        Jedis client = jedisPool.getResource();
        try {
            //
            if(client.llen(key) > 1000) {
                return ;
            }
            client.lpush(key,field);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public long llen(String key) {
        Jedis client = jedisPool.getResource();
        try {
            return client.llen(key);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /**
     * blpop key1...keyN timeout 从左至右扫描第一个非空list进行lpop操作并返回，比如blpop list1
     * list2 list3 0 如果list1 不存在或者为空且list2和list3非空，则对list2进行lpop操作并返回从
     * list2中删除的元素，如果所有的list都为空或者不存在，则会阻塞timeout 秒，timeout为0表示一直
     * 阻塞。当阻塞时，如果有client对 key1 ... keyN任意key进行push操作，则第一个在key上被阻塞的client会
     * 立即返回。（这里我猜应该是立即返回刚才client push进去的数值），如果超时发生，则返回nil.
     * @param key
     * @return
     */
    public List<String> blpop(String key,int timeout) {
        Jedis client = jedisPool.getResource();
        try {
            return client.blpop(timeout,key);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public long sadd(String key,String ... values) {
        Jedis client = jedisPool.getResource();
        try {
            return client.sadd(key,values);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /**
     * 添加listT元素的toString()结果到key对应的set中，如果成功返回1，如果元素已经存在，则返回0
     * key不存在则返回错误
     * @param key
     * @param listT
     * @param <T>
     * @return
     */
    public <T> long sadd(String key,List<T> listT) {
        if (CollectionUtils.isEmpty(listT)) {
            return 0l;    
        }
        Jedis client = jedisPool.getResource();
        try {
            String[] values = getStringList(listT);
            return client.sadd(key,values);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    private  <T> String[] getStringList(List<T> listT) {
        String[] values = new String[listT.size()];
        values = listT.stream().map(x->x.toString()).collect(Collectors.toList()).toArray(values);
        return values;
    }
    
    public long srem(String key ,String ... values) {
        Jedis client = jedisPool.getResource();
        try {
            return client.srem(key,values);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public <T> long srem(String key,List<T> listT) {
        if (CollectionUtils.isEmpty(listT)) {
            return 0L;
        }
        Jedis client = jedisPool.getResource();
        try {
            String[] values = getStringList(listT);
            return client.srem(key,values);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public Set<String> getZRangeByScore(String key,double min,double max) {
        Jedis client = jedisPool.getResource();
        try {
            return  client.zrangeByScore(key,min,max);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public long decr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.decr(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
    
    public long hlen(String key) {
        Jedis client = jedisPool.getResource();

        try {
            return client.hlen(key);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /**
     * 获取指定key的所有field1...fieldN
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget(String key,String ... fields) {
        Jedis client = jedisPool.getResource();
        try {
            return client.hmget(key,fields);
        } finally {
            jedisPool.returnResource(client);
        }
    }
    
    public Set<String> getKeysByStrPattery(String keyStrPattern) {
        Jedis client = jedisPool.getResource();
        try {
            return  client.keys(keyStrPattern);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /**
     * ltrim key start end 截取list,保留指定区间元素，成功的话返回1，key不存在返回错误。
     * @param key
     * @param begin
     * @param end
     */
    public void ltrim(String key,int begin,int end) {
        Jedis client = jedisPool.getResource();
        try {
            client.ltrim(key,begin,end);
        } finally {
            jedisPool.returnResourceObject(client);
        }
    }
    
    public long expire(String key,Integer seconds) {
        Jedis client = jedisPool.getResource();
        try {
            return client.expire(key,seconds);
        } finally {
            jedisPool.returnResource(client);
        }
    }

    /***
     * 存入hash结构时，去掉map中value的引号
     * @param key
     * @param map
     * @param <T>
     * @param <S>
     * @return
     */
    public <T,S> boolean hmsetWithoutQuotationMarks(String key,Map<T,S> map){
        if(CollectionUtils.isEmpty(map)) {
            return false;
        }
        Jedis client = jedisPool.getResource();
        try {
            Map<String,String> newMap = new HashMap<>();
            Iterator<Map.Entry<T,S>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<T,S> entry = iterator.next();
                String mKey = String.valueOf(entry.getKey());
                String mValue = JSON.toJSONString(entry.getValue()).replace("\"","");
                newMap.put(mKey,mValue);            
            }
            client.hmset(key,newMap);
            return true;
        } finally {
            jedisPool.returnResource(client);
        }
    }
}
