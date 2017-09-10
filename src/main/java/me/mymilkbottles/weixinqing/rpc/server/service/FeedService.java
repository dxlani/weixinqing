package me.mymilkbottles.weixinqing.rpc.server.service;

import me.mymilkbottles.weixinqing.alone.dao.FeedDAO;
import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.model.*;
import me.mymilkbottles.weixinqing.alone.util.EntityType;
import me.mymilkbottles.weixinqing.alone.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/07/05 20:58.
 */
@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FocusService focusService;

    @Autowired
    UserService userService;

    @Autowired
    IndexService indexService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    JedisDAO jedisAdapter;

    @Autowired
    FeedService feedService;

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

    public void deleteFeeds(int userId, int value, int weiboId) {
        feedDAO.deleteFeeds(userId, value, weiboId, null);
    }

    public void deleteFeeds(int userId, int value, int weiboId, int entityId) {
        feedDAO.deleteFeeds(userId, value, weiboId, entityId);
    }

    public int getUserFeedCount(int userId) {
        return feedDAO.getUserFeedCount(userId);
    }

    public List<ViewObject> getFeedDetail(List<Feed> feeds) {

        List<ViewObject> vos = new ArrayList<ViewObject>(10);

        //" id, user_id, type, weibo_id, exts_id, f_time, is_delete "
        User user = null;
        Weibo weibo = null;
        Comments comments = null;

        User loginUser = hostHolder.getUser();
        int loginUserId = loginUser.getId();
        int weiboValue = EntityType.WEIBO.getValue();

        for (Feed feed : feeds) {
            ViewObject vo = new ViewObject();

            int type = feed.getType();
            vo.add("type", type);
            vo.add("id", feed.getId());
            if (type == EntityType.FIRE_WEIBO.getValue()) {

            } else if (type == EntityType.COMMENT.getValue()) {
                comments = commentsService.getCommentsById(feed.getExtsId());
                vo.add("comments", comments);
            } else if (type == EntityType.TRANSMIT.getValue()) {
                vo.add("f_time", feed.getfTime());
            } else if (type == EntityType.TRANSMIT_COMMENT.getValue()) {
                comments = commentsService.getCommentsById(feed.getExtsId());
                vo.add("comments", comments);
                vo.add("f_time", feed.getfTime());
            } else if (type == EntityType.UPVOTE.getValue()) {
                vo.add("f_time", feed.getfTime());
            }

            weibo = weiboService.getWeiboById(feed.getWeiboId());
            vo.add("imgs", weiboService.getWeiboImgs(weibo.getImg()));


            int weiboId = weibo.getId();
            vo.add("comms", commentsService.getWeiboCommentCount(weiboId));
            vo.add("ups", jedisAdapter.getUpvoteCount(loginUserId, weiboValue, weiboId));
            vo.add("upd",
                    jedisAdapter.isUserUpvote(loginUserId, weiboValue, weiboId));
            vo.add("coll", jedisAdapter.isUserCollection(loginUserId, weiboValue, weiboId));
            vo.add("trs", jedisAdapter.getTransmitCount(weiboId));


            weibo.setContent(
                    weibo.getContent().replace("style=\"width:1em;height:1em;\"", "style=\"width:22px;height:22px;\""));

            vo.add("weibo", weibo);

            user = userService.getUserById(weibo.getMasterId());

            vo.add("auser", userService.getUserById(feed.getUserId()));
            vo.add("user", user);

            vos.add(vo);
        }

        return vos;
    }

    public List<Feed> getUserFeeds(int userId, int maxId, int pageSize) {
        return feedDAO.getUserFeeds(userId, maxId, pageSize + 1);
    }

    public List<Feed> getUserFeedsByList(List<Integer> userIds, int maxId, int pageSize) {
        return feedDAO.getUserFeedsByList(userIds, maxId, pageSize);
    }
}
