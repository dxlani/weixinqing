package me.mymilkbottles.weixinqing.alone.service;

import me.mymilkbottles.weixinqing.alone.async.EventModel;
import me.mymilkbottles.weixinqing.alone.async.HandlerCustomer;
import me.mymilkbottles.weixinqing.alone.async.HandlerProducer;
import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.rpcClient.RpcProxyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/06/24 10:09.
 */
@Service
public class AsyncEventService {

    @Autowired
    RpcProxyService rpcProxyService;

    public boolean sendEvent(EventModel eventModel) {
        AsyncEventService asyncEventService = rpcProxyService.create(AsyncEventService.class);
        return asyncEventService.sendEvent(eventModel);
    }
}
