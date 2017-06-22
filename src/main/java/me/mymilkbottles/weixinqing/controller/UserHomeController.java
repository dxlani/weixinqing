package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2017/06/22 16:10.
 */
@Controller
public class UserHomeController {

    @Autowired
    HostHolder hostHolder;

    @RequestMapping("/user/home/{id}")
    public String userHome(@PathVariable("id") String id) {
        int userId = WeixinqingUtil.parseUserId(id);

        return "user_home";
    }
}
