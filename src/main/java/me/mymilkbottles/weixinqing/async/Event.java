package me.mymilkbottles.weixinqing.async;


import me.mymilkbottles.weixinqing.model.EventType;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/06/13 13:32.
 */
public interface Event {

    public void doHandler(EventModel eventModel);

    public List<EventType> getSupportEventType();
}
