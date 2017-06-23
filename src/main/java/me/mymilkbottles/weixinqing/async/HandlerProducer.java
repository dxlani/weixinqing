package me.mymilkbottles.weixinqing.async;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/06/13 13:22.
 */
@Component
public class HandlerProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public void produceHandler(EventModel eventModel) {
        String json = JSONObject.toJSONString(eventModel);
        jedisAdapter.lpush(RedisKeyUtil.getHandlerKey(), json);
    }

}
