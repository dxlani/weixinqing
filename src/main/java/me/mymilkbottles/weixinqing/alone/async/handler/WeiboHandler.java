package me.mymilkbottles.weixinqing.alone.async.handler;

import me.mymilkbottles.weixinqing.alone.async.Event;
import me.mymilkbottles.weixinqing.alone.async.EventModel;

import me.mymilkbottles.weixinqing.alone.model.Feed;
import me.mymilkbottles.weixinqing.alone.service.FeedService;
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
public class WeiboHandler implements Event {

    private static final Logger log = Logger.getLogger(WeiboHandler.class);

    @Autowired
    FeedService feedService;

    @Override
    public Boolean doHandler(EventModel eventModel) {

        int userId = eventModel.getProducer();

        int weiboId = eventModel.getEventId();

        Date time = new Date(Long.valueOf(eventModel.getExt("time").toString()));

        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setWeiboId(weiboId);
        feed.setType(EntityType.FIRE_WEIBO.getValue());
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
    public List<EntityType> getSupportEntityType() {
        return Arrays.asList(new EntityType[]{EntityType.FIRE_WEIBO});
    }
}
