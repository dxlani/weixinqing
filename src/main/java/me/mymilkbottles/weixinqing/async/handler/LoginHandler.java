package me.mymilkbottles.weixinqing.async.handler;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.util.MailTemplateType;
import me.mymilkbottles.weixinqing.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/06/13 13:35.
 */
@Component
public class LoginHandler implements Event {

    @Autowired
    MailUtil mailUtil;

    @Autowired
    HostHolder hostHolder;

    @Override
    public void doHandler(EventModel eventModel) {
        int eventId = eventModel.getEventId();

        EventType eventType = eventModel.getEventType();

        int producer = eventModel.getProducer();

        int advicer = eventModel.getAdvicer();

        Map<String, Object> exts = eventModel.getExts();

        mailUtil.sendMail("3028089952@qq.com", "登录", MailTemplateType.LOGIN_MAIL, exts);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[]{EventType.LOGIN});
    }
}
