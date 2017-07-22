package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.FeedDAO;
import me.mymilkbottles.weixinqing.model.Feed;
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
}
