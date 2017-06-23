package me.mymilkbottles.weixinqing.async;

import com.alibaba.fastjson.JSON;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
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

/**
 * Created by Administrator on 2017/06/13 13:31.
 */
@Component
public class HandlerCustomer implements InitializingBean, ApplicationContextAware {

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<Event>> eventHandlerSolver = new HashMap<>();

    private ApplicationContext applicationContext;

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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String json : jedisAdapter.brpop(RedisKeyUtil.getHandlerKey())) {
                    if (RedisKeyUtil.getHandlerKey().equals(json) == false) {
                        EventModel eventModel = JSON.parseObject(json, EventModel.class);
                        for (Event event : eventHandlerSolver.get(eventModel.getEventType())) {
                            event.doHandler(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
