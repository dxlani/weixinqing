package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.FeedDAO;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/07/05 20:58.
 */
@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;

    @Autowired
    UserService userService;

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FocusService focusService;

    public int insertFeed(Feed feed) {
        return feedDAO.insertFeed(feed);
    }

    public List<Feed> getFriendsFeed(List<Integer> userFriendsId, int start, int end) {
        return feedDAO.getFeeds(userFriendsId, start, end);
    }

    public int deleteFeed(int id) {
        return feedDAO.deleteFeed(id);
    }

    public Feed getFeedById(int id) {
        return feedDAO.getFeedById(id);
    }

    public List<Feed> getFocusFeed(List<Integer> userFocusId, int start, int end) {
        return feedDAO.getFeeds(userFocusId, start, end);
    }

    public void pushFeedToActivers(int userId, String feedIdString) {
        if (userService.isUserActive(userId)) {
            List<Integer> activeFriends = focusService.getActiveFriend(userId);
            String key = null;

            for (Integer activeFriend : activeFriends) {
                key = RedisKeyUtil.getActiveFriendFeedKey(activeFriend);
                jedisDAO.lpush(key, feedIdString);
            }

            List<Integer> activeSlaveUsers = focusService.getActiveSlaveUser(userId);
            for (Integer activeSlaveUser : activeSlaveUsers) {
                key = RedisKeyUtil.getActivePersonFeedKey(activeSlaveUser);
                jedisDAO.lpush(key, feedIdString);
            }
        }
    }
}
