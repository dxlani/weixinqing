package me.mymilkbottles.weixinqing.alone.controller;

import me.mymilkbottles.weixinqing.alone.model.HostHolder;
import me.mymilkbottles.weixinqing.alone.model.Message;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.model.ViewObject;
import me.mymilkbottles.weixinqing.alone.service.MessageService;
import me.mymilkbottles.weixinqing.alone.service.UserService;
import me.mymilkbottles.weixinqing.alone.util.MessageType;
import me.mymilkbottles.weixinqing.alone.util.WeixinqingUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/06/20 11:47.
 */
@Controller
public class MessageController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    private static final int PAGE_SIZE = 10;


    @RequestMapping("/user/advice/detail/{pages}")
    public String advice(@PathVariable("pages") String pages, Model model) {
        int page = WeixinqingUtil.parsePage(pages);
        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE;
        int userId = hostHolder.getUser().getId();
        List<Message> messages = messageService.getUserTypeMessage(userId, MessageType.ADVICE.getValue(), start, end);
        List<ViewObject> vos = new ArrayList<>(messages.size());

        for (Message message : messages) {
            ViewObject vo = new ViewObject();
            vo.add("msg", message);
            vo.add("user", userService.getUserById(message.getProducer()));
            vos.add(vo);
        }

        ViewObject vo = new ViewObject();
        vo.add("self", true);
        vo.add("vos", vos);
        vo.add("type", "a");
        vo.add("luser", hostHolder.getUser());
        model.addAttribute("vo", vo);
        return "user_msg";
    }


    @RequestMapping("/user/msg/detail/{pages}")
    public String message(@PathVariable("pages") String pages, Model model) {
        int page = WeixinqingUtil.parsePage(pages);
        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE;
        int userId = hostHolder.getUser().getId();
        List<Message> messages = messageService.getUserTypeMessage(userId, MessageType.PRIVATE_MESSAGE.getValue(), start, end);
        List<ViewObject> vos = new ArrayList<>(messages.size());

        for (Message message : messages) {
            ViewObject vo = new ViewObject();
            vo.add("msg", message);
            vo.add("user", userService.getUserById(message.getProducer()));
            vos.add(vo);
        }

        ViewObject vo = new ViewObject();
        vo.add("self", true);
        vo.add("vos", vos);
        vo.add("type", "m");
        vo.add("luser", hostHolder.getUser());
        model.addAttribute("vo", vo);
        return "user_msg";
    }

    @RequestMapping("/user/msg/json/{pages}")
    @ResponseBody
    public ViewObject messageJson(@PathVariable("pages") String pages, Model model) {
        int page = WeixinqingUtil.parsePage(pages);
        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE;
        int userId = hostHolder.getUser().getId();
        List<Message> messages = messageService.getUserMessage(userId, start, end);
        List<ViewObject> vos = new ArrayList<>(messages.size());

        for (Message message : messages) {
            ViewObject vo = new ViewObject();
            vo.add("msg", message);
            vo.add("user", userService.getUserById(message.getProducer()));
            vos.add(vo);
        }

        ViewObject vo = new ViewObject();
        vo.add("self", true);
        vo.add("vos", vos);
        model.addAttribute("vo", vo);
        return vo;
    }

    @RequestMapping("/user/msg")
    @ResponseBody
    public String sendMessage(@RequestParam("uid") String uids, @RequestParam("msg") String msgs) {
        int uid = WeixinqingUtil.parseUserId(uids);
        if (uid == -1 || StringUtils.isBlank(msgs)) {
            return WeixinqingUtil.getJsonResponse(0);
        }
        return WeixinqingUtil.getJsonResponse(messageService.insertMessage(uid, msgs, new Date()));
    }

    @RequestMapping("/getadvice")
    @ResponseBody
    public ViewObject getAdvice() {

        ViewObject viewObject = new ViewObject();
        User user = hostHolder.getUser();
        if (user == null) {
            viewObject.add("Status", -1);
            return viewObject;
        }

//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        viewObject.add("Status", 0);
//        viewObject.add("atmeNumber", 10);
//        viewObject.add("commentNumber", 20);
//        viewObject.add("assistNumber", 30);

        int userId = user.getId();
        int adviceCount = messageService.getUserTypeMessageCount(userId, MessageType.ADVICE.getValue());
        int privateMessageCount = messageService.getUserTypeMessageCount(userId, MessageType.PRIVATE_MESSAGE.getValue());
        viewObject.add("unfocusNumber", adviceCount);
        viewObject.add("privateMsgNumber", privateMessageCount);

        return viewObject;
    }
}
