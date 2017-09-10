package me.mymilkbottles.weixinqing.rpc.server.service;

import me.mymilkbottles.weixinqing.alone.async.EventModel;
import me.mymilkbottles.weixinqing.alone.async.HandlerCustomer;
import me.mymilkbottles.weixinqing.alone.async.HandlerProducer;
import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.rpc.server.util.RpcService;
import me.mymilkbottles.weixinqing.alone.service.AsyncEventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/06/24 10:09.
 */
@RpcService(AsyncEventService.class)
public class AsyncEventServiceImpl {

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
