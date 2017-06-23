package me.mymilkbottles.weixinqing.async.handler;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.model.EventType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2017/06/13 13:35.
 */
@Component
public class LoginHandler implements Event {

    @Override
    public void doHandler(EventModel eventModel) {

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[]{EventType.LOGIN});
    }
}
