package me.mymilkbottles.weixinqing.util;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/06/20 22:31.
 */
@Component
public class JedisAdapter {

    //Redis服务器IP
    private static String ADDRESS = "127.0.0.1";

    //Redis的端口号
    private static int PORT = 6379;


    private static JedisPool jedisPool = null;


    static {
        try {
            jedisPool = new JedisPool(ADDRESS, PORT);
        } catch (Exception e) {
            LogUtil.error("初始化jedisPool失败" + e.getMessage());
        }
    }


    public synchronized static Jedis getJedis() {

        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            }
        } catch (Exception e) {
            LogUtil.error("获取jedis失败" + e.getMessage());
            return null;
        }
        return null;
    }

    public static void set(String key, String value) {
        Jedis jedis = JedisAdapter.getJedis();
        if (jedis != null) {
            jedis.set(key, value);
            jedis.close();
        }
    }

    public static String get(String key) {
        Jedis jedis = JedisAdapter.getJedis();
        return jedis.get(key);
    }

    public static void set(String key, String value, long time) {
        Jedis jedis = JedisAdapter.getJedis();
        if (jedis != null) {
            if (jedis.get(key) == null) {
                jedis.set(key, value, "NX", "PX", time);
            } else {
                jedis.set(key, value, "XX", "PX", time);
            }
            jedis.close();
        }
    }

    public static void llpush() {

    }

    public static void main(String[] args) {
        JedisAdapter.set("redis-key", "redis-value", 3000L);
        String value = JedisAdapter.get("redis-key");
        System.out.println(value);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
        }
        System.out.println(JedisAdapter.get("redis-key"));
    }


}
