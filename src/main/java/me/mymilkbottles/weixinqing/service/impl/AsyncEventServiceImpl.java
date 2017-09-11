package me.mymilkbottles.weixinqing.service.impl;


import me.mymilkbottles.weixinqing.model.EventModel;
import me.mymilkbottles.weixinqing.rpc.client.RpcProxyService;

import me.mymilkbottles.weixinqing.service.AsyncEventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/06/24 10:09.
 */
@Service
public class AsyncEventServiceImpl implements AsyncEventService {

    Logger logger = Logger.getLogger(AsyncEventServiceImpl.class);

    @Autowired
    RpcProxyService rpcProxyService;

    public boolean sendEvent(EventModel eventModel) {
        AsyncEventService asyncEventService = rpcProxyService.create(AsyncEventService.class);
        logger.info("asyncEventService = " + asyncEventService);
        boolean result = asyncEventService.sendEvent(eventModel);
        logger.info("result = " + result);
        return result;
    }
}
