package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.async.HandlerCustomer;
import me.mymilkbottles.weixinqing.async.HandlerProducer;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.EventModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    JedisDAO jedisAdapter;

    private static final Logger log = Logger.getLogger(AsyncEventService.class);

    public boolean sendEvent(EventModel eventModel) {
        return handlerProducer.produceHandler(eventModel);
    }
}
