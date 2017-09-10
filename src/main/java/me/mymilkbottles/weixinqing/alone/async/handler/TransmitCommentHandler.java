package me.mymilkbottles.weixinqing.alone.async.handler;

import me.mymilkbottles.weixinqing.alone.async.Event;
import me.mymilkbottles.weixinqing.alone.async.EventModel;
import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.model.*;

import me.mymilkbottles.weixinqing.alone.service.*;
import me.mymilkbottles.weixinqing.alone.util.EntityType;
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

    @Autowired
    WeiboService weiboService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    MessageService messageService;

    @Override
    public Boolean doHandler(EventModel eventModel) {

        int userId = eventModel.getProducer();

        int entityId = eventModel.getEventId();

        int entityType = Integer.valueOf((String) (eventModel.getExt("entityType")));

        Object comment = eventModel.getExt("comment");

        Date time = new Date(Long.valueOf(eventModel.getExt("time").toString()));

        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setWeiboId(entityId);

        if (comment == null) {
            feed.setType(EntityType.TRANSMIT.getValue());
        } else {
            feed.setType(EntityType.TRANSMIT_COMMENT.getValue());
            Comments comments = new Comments();
            comments.setEntityType(entityType);
            comments.setEntityId(entityId);
            comments.setfTime(time);
            comments.setContent((String) comment);
            comments.setMasterId(userId);
            commentsService.insertComments(comments);
            feed.setExtsId((int) comments.getId());
        }
        feed.setfTime(time);
        feed.setIsDelete(0);
        if (feedService.insertFeed(feed) <= 0) {
            log.error("增加feed失败");
            return Boolean.FALSE;
        }

        if (messageService.insertTransmitCommentMessage(userId, entityId, time) <= 0) {
            log.error("增加TransmitCommentMessage失败");
            return Boolean.FALSE;
        }

        feedService.pushFeedToActivers(userId, String.valueOf(feed.getId()));
        return Boolean.TRUE;
    }

    @Override
    public List<EntityType> getSupportEntityType() {
        return Arrays.asList(new EntityType[]{EntityType.TRANSMIT_COMMENT, EntityType.TRANSMIT});
    }
}
