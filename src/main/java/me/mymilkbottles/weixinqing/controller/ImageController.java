package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2017/06/29 13:26.
 */
@Controller
public class ImageController {

    @Autowired
    UserService userService;

    @RequestMapping("/hi/{id}")
    public void headImg(@PathVariable("id") String ids) {
        int id = WeixinqingUtil.parseUserId(ids);
        if (id == -1) {

        } else {
            if (userService.getUserById(id) == null) {

            } else {

            }
        }
    }

}
