package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.RequestWrapper;

/**
 * Created by Administrator on 2017/06/18 20:53.
 */
@Controller
public class IndexController {

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        LogUtil.info("index");
        return "index";
    }
}
