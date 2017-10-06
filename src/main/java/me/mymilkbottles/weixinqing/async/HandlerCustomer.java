package me.mymilkbottles.weixinqing.async;

import com.alibaba.fastjson.JSON;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.EventModel;
import me.mymilkbottles.weixinqing.util.EntityType;
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
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/06/13 13:31.
 */
@Component
public class HandlerCustomer implements InitializingBean, ApplicationContextAware {

    private static final Logger log = Logger.getLogger(HandlerCustomer.class);

    @Autowired
    JedisDAO jedisAdapter;

    private Map<EntityType, List<Event>> eventHandlerSolver = new HashMap<>();

    private static ExecutorService executors = Executors.newFixedThreadPool(3);

    private static ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Event> beans = applicationContext.getBeansOfType(Event.class);
        if (beans != null) {
            for (Map.Entry<String, Event> entry : beans.entrySet()) {
                List<EntityType> entityTypes = entry.getValue().getSupportEntityType();

                for (EntityType entityType : entityTypes) {
                    if (eventHandlerSolver.containsKey(entityType) == false) {
                        eventHandlerSolver.put(entityType, new ArrayList<Event>(2));
                    }
                    eventHandlerSolver.get(entityType).add(entry.getValue());
                }
            }
        }
        log.info("eventHandlerSolver = ");
        log.info(eventHandlerSolver);

        for (Map.Entry<EntityType, List<Event>> entry : eventHandlerSolver.entrySet()) {
            log.info("异步时间类型 " + entry.getKey() + " " + entry.getValue());
        }

        log.info("异步事件消费者启动");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    EventModel eventModel = null;
                    List<String> jsonObjects = jedisAdapter.brpop(RedisKeyUtil.getHandlerKey());

                    for (String jsonObject : jsonObjects) {
                        log.info("异步事件消费者brpop=" + jsonObject);
                        if (!RedisKeyUtil.getHandlerKey().equals(jsonObject)) {
                            eventModel = JSON.parseObject(jsonObject, EventModel.class);

                            if (!eventHandlerSolver.containsKey(eventModel.getEntityType())) {
                                log.error("不能识别的事件" + eventModel.getEntityType());
                                continue;
                            }

                            final EventModel finalEventModel = eventModel;

                            Future<Boolean> future = executors.submit(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    Boolean result = Boolean.TRUE;
                                    for (Event event : eventHandlerSolver.get(finalEventModel.getEntityType())) {
                                        result = result && event.doHandler(finalEventModel);
                                    }
                                    return result;
                                }
                            });
                            try {
                                log.info("call finish " + future.get());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
            }
        }).start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        HandlerCustomer.applicationContext = applicationContext;
    }
}
