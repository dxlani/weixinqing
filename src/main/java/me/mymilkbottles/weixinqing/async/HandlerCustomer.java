package me.mymilkbottles.weixinqing.async;

import com.alibaba.fastjson.JSON;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/06/13 13:31.
 */
@Component
public class HandlerCustomer implements InitializingBean, ApplicationContextAware {

    private static final Logger log = Logger.getLogger(HandlerCustomer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<Event>> eventHandlerSolver = new HashMap<>();

    private static ExecutorService executors = Executors.newFixedThreadPool(3);

    private static ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Event> beans = applicationContext.getBeansOfType(Event.class);
        if (beans != null) {
            for (Map.Entry<String, Event> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventType();

                for (EventType eventType : eventTypes) {
                    if (eventHandlerSolver.containsKey(eventType) == false) {
                        eventHandlerSolver.put(eventType, new ArrayList<Event>(3));
                    }
                    eventHandlerSolver.get(eventType).add(entry.getValue());
                }
            }
        }

        for (Map.Entry<EventType, List<Event>> entry : eventHandlerSolver.entrySet()) {
            log.info("异步时间类型 " + entry.getKey() + " " + entry.getValue());
        }

        log.info("异步事件消费者启动");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    EventModel eventModel = null;
                    for (String jsonObject : jedisAdapter.brpop(RedisKeyUtil.getHandlerKey())) {
                        log.info("异步事件消费者brpop=" + jsonObject);
                        if (RedisKeyUtil.getHandlerKey().equals(jsonObject) == false) {
                            eventModel = JSON.parseObject(jsonObject, EventModel.class);
                            break;
                        }
                    }

                    EventModel finalEventModel = eventModel;
                    Future<Boolean> future = executors.submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {

                            for (Event event : eventHandlerSolver.get(finalEventModel.getEventType())) {
                                event.doHandler(finalEventModel);
                                log.info("dohandler" + event + " " + finalEventModel);
                            }
                            return null;
                        }
                    });
                }
            }
        }).start();
        log.info("异步事件消费者结束");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        HandlerCustomer.applicationContext = applicationContext;
    }
}
