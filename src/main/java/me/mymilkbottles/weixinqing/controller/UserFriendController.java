package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.ViewObject;
import me.mymilkbottles.weixinqing.service.IndexService;
import me.mymilkbottles.weixinqing.service.UserService;
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

    @RequestMapping("/user/friend")
    public String friend(Model model) {
        User user = hostHolder.getUser();
        List<Feed> feeds = null;
        if (userService.isUserActive(user.getId())) {
            feeds = indexService.pushFriend(user.getId(), 0, 10);
        } else {
            feeds = indexService.pullFriend(user.getId(), 0, 10);
        }
        ViewObject vo = new ViewObject();


        model.addAttribute("vo", vo);
        return "";
    }
}
