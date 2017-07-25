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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/07/04 22:32.
 */
@Controller
public class MyFocusController {

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

    @RequestMapping("/user/myfocus")
    public String friend(Model model) {
        User localUser = hostHolder.getUser();
        List<Feed> feeds = null;
        if (userService.isUserActive(localUser.getId())) {
            feeds = indexService.pushFocus(localUser.getId(), 0, 10);
        } else {
            feeds = indexService.pullFocus(localUser.getId(), 0, 10);
        }


        List<ViewObject> vos = new ArrayList<ViewObject>(feeds.size());

        //" id, user_id, type, weibo_id, exts_id, f_time, is_delete "
        User user = null;
        Weibo weibo = null;
        Comments comments = null;
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

            weibo.setContent(
                    weibo.getContent().replace("style=\"width:1em;height:1em;\"", "style=\"width:22px;height:22px;\""));

            vo.add("weibo", weibo);


            user = userService.getUserById(weibo.getMasterId());
            vo.add("user", user);

            vos.add(vo);
        }

//        System.out.println(vos);
        model.addAttribute("PageType", "myfocus");
        model.addAttribute("vos", vos);

        return "user_friend";
    }
}
