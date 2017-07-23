package me.mymilkbottles.weixinqing.async.handler;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.Comments;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.service.FeedService;
import me.mymilkbottles.weixinqing.service.FocusService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.EntityType;
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
public class TransmitCommentHandler implements Event {

    private static final Logger log = Logger.getLogger(TransmitCommentHandler.class);

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

        int userId = eventModel.getProducer();

        int weiboId = eventModel.getEventId();

        Object comment = eventModel.getExt("comment");

        Date time = (Date) eventModel.getExt("time");


        jedisDAO.transmit(weiboId, userId);
        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setWeiboId(weiboId);

        if (comment == null) {
            feed.setType(EventType.TRANSMIT.getValue());
        } else {
            feed.setType(EventType.TRANSMIT_COMMENT.getValue());
            Comments comments = new Comments();
            comments.setWeiboId(weiboId);
            comments.setfTime(time);
            comments.setContent((String) comment);
            comments.setMasterId(userId);
            feed.setExtsId((int) comments.getId());
        }
        feed.setfTime(time);
        feed.setIsDelete(0);
        if (feedService.insertFeed(feed) < 0) {
            log.error("增加feed失败");
            return Boolean.FALSE;
        }
        feedService.pushFeedToActivers(userId, String.valueOf(feed.getId()));
        return Boolean.TRUE;
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[]{EventType.TRANSMIT_COMMENT, EventType.TRANSMIT});
    }
}
