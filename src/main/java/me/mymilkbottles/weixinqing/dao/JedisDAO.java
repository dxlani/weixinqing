package me.mymilkbottles.weixinqing.dao;


import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
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

    public int upvote(int userId, int entityType, int entityId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getUpVoteKey(entityType, entityId);
                if (jedis.sadd(key, String.valueOf(userId)).longValue() == 0) {
                    jedis.srem(key, String.valueOf(userId));
                    return 0;
                }
                return 1;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return -1;
    }


    public int getUpvoteCount(int userId, int entityType, int entityId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getUpVoteKey(entityType, entityId);
                return jedis.scard(key).intValue();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return 0;
    }


    public int collection(int userId, int entityType, int entityId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getCollectionKey(entityType, entityId);
                if (jedis.sadd(key, String.valueOf(userId)).longValue() == 0) {
                    jedis.srem(key, String.valueOf(userId));
                    return 1;
                }
                return 1;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return 0;
    }


    public Boolean isUserCollection(int userId, int entityType, int entityId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getCollectionKey(entityType, entityId);
                return jedis.sismember(key, String.valueOf(userId));
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return Boolean.FALSE;
    }


    public Boolean isUserUpvote(int userId, int entityType, int entityId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getUpVoteKey(entityType, entityId);
                return jedis.sismember(key, String.valueOf(userId));
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return Boolean.FALSE;
    }


    public List<String> getActivers() {
        String activePersonKey = RedisKeyUtil.getActivePersonKey();
        return lrange(activePersonKey, 0, -1);
    }
    public void set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value, long time) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                if (jedis.get(key) == null) {
                    jedis.set(key, value, "NX", "PX", time);
                } else {
                    jedis.set(key, value, "XX", "PX", time);
                }
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }

    public long srem(String key, String json) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.srem(key, json);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return -1;
    }

    public long sadd(String key, String json) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.sadd(key, json);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return -1;
    }

    public boolean sismember(String key, String member) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.sismember(key, member);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return false;
    }

    public Set<String> smembers(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.smembers(key);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return null;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.lrange(key, start, end);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return null;
    }

    public Set<String> sinter(String key, String member) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.sinter(key, member);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return null;
    }

    public void lpush(String key, String json) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                jedis.lpush(key, json);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }

    public void lrem(int count, String listKey, String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                jedis.lrem(listKey, count, key);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }


    public List<String> brpop(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.brpop(0, key);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return new ArrayList<String>();
    }

    public int transmit(int weiboId, int userId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getUserTransmitKey(weiboId);
                return jedis.sadd(key, String.valueOf(userId)).intValue();
            } finally {
                jedis.close();
            }
        }
        return 0;
    }

    public int getTransmitCount(int weiboId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getUserTransmitKey(weiboId);
                return jedis.scard(key).intValue();
            } finally {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean isUserTransmited(int weiboId, int userId) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                String key = RedisKeyUtil.getUserTransmitKey(weiboId);
                return jedis.sismember(key, String.valueOf(userId));
            } finally {
                jedis.close();
            }
        }
        return false;
    }


//    public static void main(String[] args) {
//        new JedisDAO().getJedis().incr("hello world test");
//    }
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
