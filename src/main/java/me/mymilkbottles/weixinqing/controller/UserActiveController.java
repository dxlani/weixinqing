package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.Comments;
import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.CommentsService;
import me.mymilkbottles.weixinqing.service.FeedService;
import me.mymilkbottles.weixinqing.service.UserActiveService;
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
    UserActiveService userActiveService;

    @RequestMapping("/user/upvote/{type}/{id}")
    @ResponseBody
    public String upVote(@PathVariable("type") String types, @PathVariable("id") String ids) throws Exception {

        int userId = hostHolder.getUser().getId();
        userActiveService.upVote(types, ids, userId);
        return WeixinqingUtil.getJSONString(200);
    }


    @RequestMapping("/user/downvote/{type}/{id}")
    @ResponseBody
    public String downVote(@PathVariable("type") String types, @PathVariable("id") String ids) throws IllegalArgumentException {

        int userId = hostHolder.getUser().getId();
        userActiveService.downVote(types, ids, userId);
        return WeixinqingUtil.getJSONString(200);
    }


    @RequestMapping("/user/transmit/{weiboId}")
    @ResponseBody
    public String transmit(@PathVariable("weiboId") String weiboIds) throws IllegalArgumentException {
        int userId = hostHolder.getUser().getId();
        userActiveService.transmit(weiboIds, userId);
        return WeixinqingUtil.getJSONString(200);
    }


    @RequestMapping("/user/commit/{userId}/{weiboId}")
    @ResponseBody
    public String commit(@PathVariable("userId") String userIds,
                         @PathVariable("weiboId") String weiboIds, String content) throws IllegalArgumentException {
        userActiveService.comments(userIds, weiboIds, content);
        return WeixinqingUtil.getJSONString(200);
    }

    @RequestMapping("/user/collection/{type}/{id}")
    @ResponseBody
    public String collection(@PathVariable("type") String types, @PathVariable("id") String ids) {
        int userId = hostHolder.getUser().getId();
        userActiveService.collection(types, ids, userId);
        return WeixinqingUtil.getJSONString(200);
    }
}
