package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.*;
import me.mymilkbottles.weixinqing.service.CommentsService;
import me.mymilkbottles.weixinqing.service.IndexService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.service.WeiboService;
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

    @RequestMapping("/user/friend")
    public String friend(Model model) {
        User localUser = hostHolder.getUser();
        List<Feed> feeds = null;
        if (userService.isUserActive(localUser.getId())) {
            feeds = indexService.pushFriend(localUser.getId(), 0, 10);
        } else {
            feeds = indexService.pullFriend(localUser.getId(), 0, 10);
        }
        List<ViewObject> vos = new ArrayList<ViewObject>(10);

        //" id, user_id, type, weibo_id, exts_id, f_time, is_delete "
        User user = null;
        Weibo weibo = null;
        Comments comments = null;
        for (Feed feed : feeds) {
            ViewObject vo = new ViewObject();

            int type = feed.getType();
            vo.add("type", type);
            if (type == EntityType.FIRE_WEIBO.getValue()) {

            } else if (type == EntityType.COMMENT.getValue()) {
                comments = commentsService.getCommentsById(feed.getExtsId());
                vo.add("comments", comments);
            } else if (type == EntityType.FORWARD.getValue()) {
                vo.add("f_time", feed.getfTime());
            } else if (type == EntityType.FORWARD_COMMENTS.getValue()) {
                comments = commentsService.getCommentsById(feed.getExtsId());
                vo.add("comments", comments);
                vo.add("f_time", feed.getfTime());
            } else if (type == EntityType.UPVOTE.getValue()) {
                vo.add("f_time", feed.getfTime());
            }
            weibo = weiboService.getWeiboById(feed.getUserId());
            vo.add("weibo", weibo);

            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "user_friend";
    }
}
