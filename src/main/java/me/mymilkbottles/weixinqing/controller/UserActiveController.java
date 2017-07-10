package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.Comments;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.CommentsService;
import me.mymilkbottles.weixinqing.service.FeedService;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/10 18:11.
 */
@Controller
public class UserActiveController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FeedService feedService;

    @Autowired
    CommentsService commentsService;

    @RequestMapping("/user/upvote/{type}/{id}")
    @ResponseBody
    public String upVote(@PathVariable("type") String types, @PathVariable("id") String ids) throws Exception {
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

        int userId = hostHolder.getUser().getId();
        String upVoteKey = RedisKeyUtil.getUpVoteKey(type, id);
        jedisDAO.lpush(upVoteKey, String.valueOf(userId));
        return WeixinqingUtil.getJSONString(200);
    }


    @RequestMapping("/user/downvote/{type}/{id}")
    @ResponseBody
    public String downVote(@PathVariable("type") String types, @PathVariable("id") String ids) throws IllegalArgumentException {
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
        int userId = hostHolder.getUser().getId();
        String upVoteKey = RedisKeyUtil.getUpVoteKey(type, id);
        jedisDAO.lrem(0, upVoteKey, String.valueOf(userId));
        return WeixinqingUtil.getJSONString(200);
    }


    @RequestMapping("/user/transmit/{weiboId}")
    @ResponseBody
    public String transmit(@PathVariable("weiboId") String weiboIds) throws IllegalArgumentException {
        int weiboId = -1;
        try {
            weiboId = Integer.parseInt(weiboIds);
        } catch (Exception e) {
        }
        if (weiboId == -1) {
            throw new IllegalArgumentException("weiboId参数不合法");
        }
        User user = hostHolder.getUser();
        Feed feed = new Feed();
        feed.setUserId(user.getId());
        feed.setfTime(new Date());
        feed.setWeiboId(weiboId);
        feed.setIsDelete(0);
        feed.setType(EntityType.FORWARD.getValue());
        feedService.insertFeed(feed);
        return WeixinqingUtil.getJSONString(200);
    }


    @RequestMapping("/user/commit/{userId}/{weiboId}")
    @ResponseBody
    public String commit(@PathVariable("userId") String userIds,
                         @PathVariable("weiboId") String weiboIds, String content) throws IllegalArgumentException {
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

        feedService.insertFeed(new Feed(hostHolder.getUser().getId(), EntityType.COMMENT.getValue(), weiboId, (int) comment.getId(), new Date(), 0));
        return WeixinqingUtil.getJSONString(200);
    }

    @RequestMapping("/user/collection/{type}/{id}")
    @ResponseBody
    public String collection(@PathVariable("type") String types, @PathVariable("id") String ids) {
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
        User user = hostHolder.getUser();
        String collectionKey = RedisKeyUtil.getCollectionKey(user.getId());
        jedisDAO.lpush(collectionKey, ids);
        feedService.insertFeed(new Feed(user.getId(), EntityType.COLLECTION.getValue(), id, 0, new Date(), 0));
        return WeixinqingUtil.getJSONString(200);
    }
}
