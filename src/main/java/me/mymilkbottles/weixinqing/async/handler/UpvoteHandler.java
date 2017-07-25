package me.mymilkbottles.weixinqing.async.handler;

import me.mymilkbottles.weixinqing.async.Event;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.service.FeedService;
import me.mymilkbottles.weixinqing.service.MessageService;
import me.mymilkbottles.weixinqing.util.EntityType;
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
public class UpvoteHandler implements Event {

    private static final Logger log = Logger.getLogger(UpvoteHandler.class);

    @Autowired
    FeedService feedService;

    @Autowired
    MessageService messageService;

    @Override
    public Boolean doHandler(EventModel eventModel) {
        int userId = eventModel.getProducer();

        int weiboId = eventModel.getEventId();

        Date time = new Date(Long.valueOf(eventModel.getExt("time").toString()));

        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setWeiboId(weiboId);
        feed.setType(EntityType.UPVOTE.getValue());
        feed.setfTime(time);
        feed.setIsDelete(0);

        if (feedService.insertFeed(feed) < 0) {
            log.error("增加feed失败");
            return Boolean.FALSE;
        }

        if (messageService.insertUpvoteMessage(userId, weiboId, time) <= 0) {
            log.error("增加UpvoteMessage失败");
            return Boolean.FALSE;
        }

        feedService.pushFeedToActivers(userId, String.valueOf(feed.getId()));

        return Boolean.TRUE;
    }

    @Override
    public List<EntityType> getSupportEntityType() {
        return Arrays.asList(new EntityType[]{EntityType.UPVOTE});
    }
}
