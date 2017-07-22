package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.FeedDAO;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/07/05 19:37.
 */
@Service
public class IndexService {

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FeedService feedService;

    @Autowired
    FocusService focusService;

    @Autowired
    HostHolder hostHolder;

    public List<Feed> pushFocus(int userId, int start, int end) {
        String activePersonFeedKey = RedisKeyUtil.getActivePersonFeedKey(hostHolder.getUser().getId());
        List<Feed> feeds = new ArrayList<>(end - start);
        for (String id : jedisDAO.lrange(activePersonFeedKey, start, end)) {
            feeds.add(feedService.getFeedById(Integer.valueOf(id)));
        }
        return feeds;
    }

    public List<Feed> pullFocus(int userId, int start, int end) {
        List<Integer> masterUser = focusService.getMasterUser(userId);
        return feedService.getFocusFeed(masterUser, start, end);
    }

    public List<Feed> pushFriend(int userId, int start, int end) {
        String activeFriendFeedKey = RedisKeyUtil.getActiveFriendFeedKey(hostHolder.getUser().getId());
        List<Feed> feeds = new ArrayList<>(end - start);
        for (String id : jedisDAO.lrange(activeFriendFeedKey, start, end)) {
            feeds.add(feedService.getFeedById(Integer.valueOf(id)));
        }
        return feeds;
    }

    public List<Feed> pullFriend(int userId, int start, int end) {
        List<Integer> friendUsers = focusService.getFriendUsers(userId);
        return feedService.getFriendsFeed(friendUsers, start, end);
    }
}
