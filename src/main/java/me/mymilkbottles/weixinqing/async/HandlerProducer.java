package me.mymilkbottles.weixinqing.async;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2017/06/13 13:22.
 */
@Component
public class HandlerProducer {

    private static final Logger log = Logger.getLogger(HandlerProducer.class);

    @Autowired
    JedisDAO jedisAdapter;

    public boolean produceHandler(EventModel eventModel) {
        Jedis jedis = jedisAdapter.getJedis();
        if (jedis == null) {
            return false;
        }
        String key = RedisKeyUtil.getHandlerKey();
        String json = JSONObject.toJSONString(eventModel);
        try {
            jedis.lpush(key, json);
        } catch (Exception e) {
            jedis.lrem(key, 0, json);
            return false;
        } finally {
            jedis.close();
        }
        jedis = jedisAdapter.getJedis();
        long res = jedis.llen(key);
        int len = jedis.llen(key).intValue();
        for (int i = 0; i < len; ++i) {
            if (json.equals(jedis.lindex(key, i))) {
                jedis.close();
                return true;
            }
        }
        jedis.close();
        return false;
    }
}
