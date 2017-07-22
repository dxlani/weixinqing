package me.mymilkbottles.weixinqing.controller;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.Weibo;
import me.mymilkbottles.weixinqing.service.WeiboService;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.SensitiveWordFilterUtil;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Administrator on 2017/07/22 16:47.
 */
@Controller
public class WeiboController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    WeiboService weiboService;

    @RequestMapping("/user/fireweibo")
    @ResponseBody
    public String fireWeibo(@ModelAttribute("content") String content, HttpServletRequest request,
                            Model model) {
        Weibo weibo = new Weibo();
        weibo.setContent(SensitiveWordFilterUtil.filter(content));
        weibo.setfTime(new Date());
        weibo.setMasterId(hostHolder.getUser().getId());
        int code = weiboService.insertWeibo(weibo) > 0 ? 200 : 0;
        return WeixinqingUtil.getJsonResponse(code);
    }

    @RequestMapping("/user/upvote/{weiboIds}")
    @ResponseBody
    public String upvote(@PathVariable("weiboIds") String weiboIds) {
        int weiboId = WeixinqingUtil.parseWeiboId(weiboIds);
        if (weiboId != -1) {
            weiboService.upvote(hostHolder.getUser().getId(), EntityType.UPVOTE.getValue(), weiboId);
        } else {
            return WeixinqingUtil.getJsonResponse(0);
        }
        return null;
    }
}
