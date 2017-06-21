package me.mymilkbottles.weixinqing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/06/21 11:20.
 */
@Controller
public class RegisterController {

    @RequestMapping("/register")
    public String register() {
        return "register";
    }
}
