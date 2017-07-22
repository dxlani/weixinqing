package me.mymilkbottles.weixinqing.async.handler;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.service.FeedService;
import me.mymilkbottles.weixinqing.service.FocusService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.MailUtil;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2017/06/13 13:35.
 */
@Component
public class WeiboHandler implements Event {

    private static final Logger log = Logger.getLogger(WeiboHandler.class);

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FocusService focusService;

    @Override
    public Boolean doHandler(EventModel eventModel) {

        int userId = eventModel.getEventId();

        int weiboId = eventModel.getEventId();

        Date time = (Date) eventModel.getExt("time");

        if (userService.isUserActive(userId)) {
            List<Integer> activeFriends = focusService.getActiveFriend(userId);
            String key = null;

            String weiboString = String.valueOf(weiboId);
            for (Integer activeFriend : activeFriends) {
                key = RedisKeyUtil.getActiveFriendFeedKey(activeFriend);
                jedisDAO.lpush(key, weiboString);
            }

            List<Integer> activeSlaveUsers = focusService.getActiveSlaveUser(userId);
            for (Integer activeSlaveUser : activeSlaveUsers) {
                key = RedisKeyUtil.getActivePersonFeedKey(activeSlaveUser);
                jedisDAO.lpush(key, weiboString);
            }
        }

        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setWeiboId(weiboId);
        feed.setType(EntityType.FIRE_WEIBO.getValue());
        feed.setfTime(time);
        feed.setIsDelete(0);

        if (feedService.insertFeed(feed) < 0) {
            log.error("增加feed失败");
        }

        return Boolean.TRUE;
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[]{EventType.FIRE_WEIBO});
    }
}
