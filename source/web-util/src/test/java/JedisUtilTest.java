import com.swj.ics.jedis_util.JedisUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2016/11/27.
 */
/*
public class JedisUtilTest {
    private static  String ipAddr="192.168.0.105";
    private static int port=6379;
    private static Jedis jedis=null;

    //@BeforeClass
    public static void init()
    {
        jedis= JedisUtil.getInstance().getJedis(ipAddr,port);
    }
    //@AfterClass
    public static void close()
    {
        JedisUtil.getInstance().closeJedis(ipAddr,port, jedis);
    }
    private void println(String s)
    {
        System.out.println(s);
    }
    //@org.junit.Test
    public void testKeys() throws  InterruptedException
    {
        println("flush the db:"+jedis.flushDB());
        println("判断username键是否存在:"+jedis.get("username"));
        println("新增<username,'swj'>键值对:"+jedis.set("username","swj"));
        println("key username exists:"+jedis.exists("username"));
        println("新增<password,'password'>键值对:"+jedis.set("password","password"));
        println("系统中有如下的key:");
        Set<String> sets= jedis.keys("*");
        System.out.println(sets);
        println("删除键password："+jedis.del("password"));
        println("key password exists:"+jedis.exists("password"));
        println("设置username的过期时间为5秒钟:"+jedis.expire("username",5));

        TimeUnit.SECONDS.sleep(3);

        println("查看username键的剩余时间秒数："+jedis.ttl("username"));
        println("查看键username的类型:"+jedis.type("username"));
    }

   // @Test
    public void test_Int_And_Decimal_With_Jedis()
    {
        jedis.flushDB();
        jedis.set("key1","1");
        jedis.set("key2","2");
        jedis.set("key3","2.3");
        println("key1 的值增加1："+jedis.incrBy("key1",1));
        println("key1的值是"+jedis.get("key1"));
        println("key2的值减去1："+jedis.decrBy("key2",1));
        println("key2的值为："+jedis.get("key2"));
        println("key1的值增加5："+jedis.incrBy("key1",5));
        println("key1的值是"+jedis.get("key1"));
        println("key2的值减去5："+jedis.decrBy("key2",5));
        println("key2的值是"+jedis.get("key2"));

        println("key3的值增加2.2"+jedis.incrByFloat("key3",2.2));
        println("key3的值为:"+jedis.get("key3"));
    }
}*/
