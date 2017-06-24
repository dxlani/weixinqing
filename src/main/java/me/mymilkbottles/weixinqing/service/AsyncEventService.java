package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.async.HandlerCustomer;
import me.mymilkbottles.weixinqing.async.HandlerProducer;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Transaction;

/**
 * Created by Administrator on 2017/06/24 10:09.
 */
@Service
public class AsyncEventService {

    @Autowired
    HandlerCustomer handlerCustomer;

    @Autowired
    HandlerProducer handlerProducer;

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger log = Logger.getLogger(AsyncEventService.class);

    public boolean sendEvent(EventModel eventModel) {
        return handlerProducer.produceHandler(eventModel);
    }
}
