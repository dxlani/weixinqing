package me.mymilkbottles.weixinqing.alone.controller;

import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.model.*;
import me.mymilkbottles.weixinqing.alone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Autowired
    FocusService focusService;

    @RequestMapping("/user/friend")
    public String friend(Model model) {
        User localUser = hostHolder.getUser();
        List<Feed> feeds = null;
        if (userService.isUserActive(localUser.getId())) {
            feeds = indexService.pushFriend(localUser.getId(), 0, 10);
        } else {
            feeds = indexService.pullFriend(localUser.getId(), 0, 10);
        }


        int userId = hostHolder.getUser().getId();
        List<Integer> masterUsers = focusService.getMasterUser(userId);


        ViewObject vo = new ViewObject();
        vo.add("vo", feedService.getFeedDetail(feeds));
        vo.add("luser", userService.getUserById(userId));
        vo.add("focusd", focusService.isFocus(userId, hostHolder.getUser().getId()));
        model.addAttribute("v", vo);
        model.addAttribute("PageType", "myfriends");
        return "focus_dynamic";
    }

}
