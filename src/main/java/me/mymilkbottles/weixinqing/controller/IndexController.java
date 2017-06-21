package me.mymilkbottles.weixinqing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.RequestWrapper;

/**
 * Created by Administrator on 2017/06/18 20:53.
 */
@Controller
public class IndexController {

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        System.out.println("index");
        return "index";
    }
}
