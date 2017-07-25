package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.model.*;
import me.mymilkbottles.weixinqing.service.*;
import me.mymilkbottles.weixinqing.util.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/07/04 22:32.
 */
@Controller
public class UserFriendController {

    @Autowired
    HostHolder hostHolder;

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

    @RequestMapping("/user/friend")
    public String friend(Model model) {
        User localUser = hostHolder.getUser();
        List<Feed> feeds = null;
        if (userService.isUserActive(localUser.getId())) {
            feeds = indexService.pushFriend(localUser.getId(), 0, 10);
        } else {
            feeds = indexService.pullFriend(localUser.getId(), 0, 10);
        }


        List<ViewObject> vos = feedService.getFeedDetail(feeds);

        model.addAttribute("PageType", "myfriends");
        model.addAttribute("vos", vos);

        return "user_friend";
    }
}
