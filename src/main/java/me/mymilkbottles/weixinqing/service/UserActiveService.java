package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.Comments;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/10 22:33.
 */
@Service
public class UserActiveService {

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FeedService feedService;

    @Autowired
    CommentsService commentsService;

    public void upVote(String types, String ids, int userId) throws Exception {
        int type = -1;
        int id = -1;
        try {
            type = Integer.parseInt(types);
            id = Integer.parseInt(ids);
        } catch (Exception e) {
        }
        if (type == -1 || id == -1) {
            throw new IllegalArgumentException("type或id参数不合法");
        }

        String upVoteKey = RedisKeyUtil.getUpVoteKey(type, id);
        jedisDAO.sadd(upVoteKey, String.valueOf(userId));
    }


    public void downVote(String types, String ids, int userId) throws IllegalArgumentException {
        int type = -1;
        int id = -1;
        try {
            type = Integer.parseInt(types);
            id = Integer.parseInt(ids);
        } catch (Exception e) {
        }
        if (type == -1 || id == -1) {
            throw new IllegalArgumentException("type或id参数不合法");
        }
        String voteKey = RedisKeyUtil.getUpVoteKey(type, id);
        jedisDAO.srem(voteKey, String.valueOf(userId));
    }

    public void transmit(String weiboIds, int userId) throws IllegalArgumentException {
        int weiboId = -1;
        try {
            weiboId = Integer.parseInt(weiboIds);
        } catch (Exception e) {
        }
        if (weiboId == -1) {
            throw new IllegalArgumentException("weiboId参数不合法");
        }
        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setfTime(new Date());
        feed.setWeiboId(weiboId);
        feed.setIsDelete(0);
        feed.setType(EntityType.FORWARD.getValue());
        feedService.insertFeed(feed);
    }

    public void comments(String userIds, String weiboIds, String content) throws IllegalArgumentException {
        int weiboId = -1;
        int userId = -1;
        try {
            weiboId = Integer.parseInt(weiboIds);
            userId = Integer.parseInt(userIds);
        } catch (Exception e) {
        }
        if (weiboId == -1) {
            throw new IllegalArgumentException("userId,weiboId参数不合法");
        }
        Comments comment = new Comments();
        comment.setfTime(new Date());
        comment.setWeiboId(weiboId);
        comment.setContent(content);
        comment.setMasterId(userId);
        commentsService.insertComments(comment);

        feedService.insertFeed(new Feed(userId, EntityType.COMMENT.getValue(), weiboId, (int) comment.getId(), new Date(), 0));
    }

    public void collection(String types, String ids, int userId) {
        int type = -1;
        int id = -1;
        try {
            type = Integer.parseInt(types);
            id = Integer.parseInt(ids);
        } catch (Exception e) {
        }
        if (type == -1 || id == -1) {
            throw new IllegalArgumentException("type,id参数不合法");
        }
        String collectionKey = RedisKeyUtil.getCollectionKey(userId);
        jedisDAO.lpush(collectionKey, ids);
        feedService.insertFeed(new Feed(userId, EntityType.COLLECTION.getValue(), id, 0, new Date(), 0));
    }
}
