package me.mymilkbottles.weixinqing.dao;


import me.mymilkbottles.weixinqing.model.Feed;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/06/20 22:31.
 */
@Component
public class JedisDAO {

    //Redis服务器IP
    private static String ADDRESS = "127.0.0.1";

    //Redis的端口号
    private static int PORT = 6379;

    private static JedisPool jedisPool = null;

    private static final Logger log = Logger.getLogger(JedisDAO.class);

    static {
        try {
            jedisPool = new JedisPool(ADDRESS, PORT);
        } catch (Exception e) {
            log.error("初始化jedisPool失败" + e.getMessage());
        }
    }


    public synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            }
        } catch (Exception e) {
            log.error("获取jedis失败" + e.getMessage());
        }
        return null;
    }

    public Transaction getTransaction() {
        Jedis jedis = getJedis();
        if (jedis != null) {
            return jedis.multi();
        }
        return null;
    }

    public List<Object> exec(Transaction tx) {
        if (tx != null) {
            return tx.exec();
        }
        return null;
    }


    public void set(String key, String value) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.set(key, value);
            jedis.close();
        }
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public void set(String key, String value, long time) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            if (jedis.get(key) == null) {
                jedis.set(key, value, "NX", "PX", time);
            } else {
                jedis.set(key, value, "XX", "PX", time);
            }
            jedis.close();
        }
    }

    public long srem(String key, String json) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            long result = jedis.srem(key, json);
            jedis.close();
            return result;
        }
        return -1;
    }

    public long sadd(String key, String json) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            long result = jedis.sadd(key, json);
            jedis.close();
            return result;
        }
        return -1;
    }

    public boolean sismember(String key, String member) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            boolean result = jedis.sismember(key, member);
            jedis.close();
            return result;
        }
        return false;
    }

    public Set<String> smembers(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            Set<String> set = jedis.smembers(key);
            jedis.close();
            return set;
        }
        return null;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            List<String> list = jedis.lrange(key, start, end);
            jedis.close();
            return list;
        }
        return null;
    }

    public Set<String> sinter(String key, String member) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            Set<String> set = jedis.sinter(key, member);
            jedis.close();
            return set;
        }
        return null;
    }

    public void lpush(String key, String json) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.lpush(key, json);
            jedis.close();
        }
    }

    public void lrem(int count, String listKey, String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.lrem(listKey, count, key);
            jedis.close();
        }
    }

    public List<String> brpop(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            List<String> list = jedis.brpop(0, key);
            jedis.close();
            return list;
        }
        return new ArrayList<String>();
    }

//    public static void main(String[] args) {
//        set("redis-key", "redis-value", 3000L);
//        String value = get("redis-key");
//        System.out.println(value);
//
//        try {
//            Thread.sleep(2500);
//        } catch (InterruptedException e) {
//        }
//        System.out.println(JedisAdapter.get("redis-key"));
//    }


}
