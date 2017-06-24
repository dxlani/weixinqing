package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.intercepter.LoginIntercepter;
import me.mymilkbottles.weixinqing.model.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/06/18 20:53.
 */
@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(LoginIntercepter.class);

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        log.info("index");
        return "index";
    }
}
