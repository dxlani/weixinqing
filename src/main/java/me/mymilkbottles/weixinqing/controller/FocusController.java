package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.ViewObject;
import me.mymilkbottles.weixinqing.service.*;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/07/04 22:32.
 */
@Controller
public class FocusController {

    private static final Logger log = Logger.getLogger(FocusController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    IndexService indexService;

    @Autowired
    FocusService focusService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    CommentsService commentsService;

    private static final int PAGE_SIZE = 20;

    @RequestMapping("/user/follow/{userIds}/{pages}")
    public String focus(Model model, @PathVariable("userIds") String userIds,
                        @PathVariable("pages") String pages) {

        int userId = WeixinqingUtil.parseUserId(userIds);

        if (userId == -1) {
            return "404";
        }

        int page = WeixinqingUtil.parsePage(pages);

        int count = focusService.getSlaveUserCount(userId);
        int start = PAGE_SIZE * (page - 1);
        if (start >= count) {
            start = 0;
            page = 1;
        }
        int end = PAGE_SIZE * page;
        List<User> users = focusService.getSlaveUser(userId, start, end);


        List<ViewObject> vos = new ArrayList<>(users.size());


        int localUserId = hostHolder.getUser().getId();
        for (User user : users) {
            ViewObject vo = new ViewObject();
            vo.add("user", user);
            vo.add("focusd", focusService.isFocus(localUserId, user.getId()));
            vos.add(vo);
        }

        ViewObject vo = new ViewObject();
        vo.add("page", page);
        boolean isSelf = localUserId == userId;
        vo.add("self", isSelf);
        if (!isSelf) {
            vo.add("focusd", focusService.isFocus(localUserId, userId));
        }
        vo.add("luser", userService.getUserById(userId));
        vo.add("vos", vos);
        model.addAttribute("vo", vo);
        return "user_focus";
    }


    @RequestMapping("/user/focus/action/{uids}")
    @ResponseBody
    public String focus(Model model, @PathVariable("uids") String uids) {
        int userId = WeixinqingUtil.parseUserId(uids);
        if (userId == -1) {
            return "404";
        }
        int localUserId = hostHolder.getUser().getId();
        if (focusService.isFocus(userId, localUserId)) {
            focusService.unFocus(userId, localUserId);
        } else {
            focusService.focus(userId, localUserId);
        }
        return WeixinqingUtil.getJsonResponse(200);
    }

    @RequestMapping("/user/focus/{userIds}/{pages}")
    public String follow(Model model, @PathVariable("userIds") String userIds,
                         @PathVariable("pages") String pages) {

        int userId = WeixinqingUtil.parseUserId(userIds);

        if (userId == -1) {
            return "404";
        }

        int page = WeixinqingUtil.parsePage(pages);

        int count = focusService.getMasterUserCount(userId);
        int start = PAGE_SIZE * (page - 1);
        if (start >= count) {
            start = 0;
            page = 1;
        }
        int end = PAGE_SIZE * page;
        List<User> users = focusService.getMasterUser(userId, start, end);

        List<ViewObject> vos = new ArrayList<>(users.size());

        int localUserId = hostHolder.getUser().getId();
        for (User user : users) {
            ViewObject vo = new ViewObject();
            vo.add("user", user);
            vo.add("focusd", focusService.isFocus(localUserId, user.getId()));
            vos.add(vo);
        }

        ViewObject vo = new ViewObject();
        vo.add("page", page);
        boolean isSelf = hostHolder.getUser().getId() == userId;
        vo.add("self", isSelf);
        if (!isSelf) {
            vo.add("focusd", focusService.isFocus(localUserId, userId));
        }
        vo.add("vos", vos);
        vo.add("luser", userService.getUserById(userId));
        model.addAttribute("vo", vo);
        return "user_focus";
    }
}
