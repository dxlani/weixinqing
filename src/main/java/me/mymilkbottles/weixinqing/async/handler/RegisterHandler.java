package me.mymilkbottles.weixinqing.async.handler;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.ActivationService;
import me.mymilkbottles.weixinqing.util.MailTemplateType;
import me.mymilkbottles.weixinqing.util.MailUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.*;

/**
 * Created by Administrator on 2017/06/29 13:35.
 */
@Component
public class RegisterHandler implements Event {

    private static final Logger log = Logger.getLogger(RegisterHandler.class);

    @Autowired
    ActivationService activationService;

    @Autowired
    MailUtil mailUtil;

    @Override
    public Boolean doHandler(EventModel eventModel) {
        Map<String, Object> eventModelExts = eventModel.getExts();

        String mail = (String) eventModelExts.get("activationMail");
        String key = (String) eventModelExts.get("activationKey");

        Map<String, Object> exts = new HashMap<String, Object>();
        exts.put("url", eventModelExts.get("url"));

        return mailUtil.sendMail(mail, "微心情激活邮件", MailTemplateType.REGISTER_MAIL, exts);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.REGISTER);
    }
}
