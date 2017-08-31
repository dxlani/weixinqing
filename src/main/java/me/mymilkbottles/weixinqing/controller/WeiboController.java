package me.mymilkbottles.weixinqing.controller;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.Weibo;
import me.mymilkbottles.weixinqing.service.WeiboService;
import me.mymilkbottles.weixinqing.util.ContentFilter;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.apache.commons.lang3.StringUtils;
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
        weibo.setContent(ContentFilter.filter(content));
        weibo.setfTime(new Date());
        weibo.setMasterId(hostHolder.getUser().getId());
        int code = weiboService.insertWeibo(weibo) > 0 ? 200 : 0;
        return WeixinqingUtil.getJsonResponse(code);
    }

    @RequestMapping("/user/transmit/{weiboIds}")
    @ResponseBody
    public String transmit(@ModelAttribute(value = "comment") String comment,
                           @PathVariable("weiboIds") String weiboIds,
                           HttpServletRequest request,
                           Model model) {
        int weiboId = WeixinqingUtil.parseWeiboId(weiboIds);
        if (weiboId != -1) {
            int transmit = weiboService.transmit(weiboId, comment);
            if (transmit >= 0) {
                if (transmit == 0) {
                    return WeixinqingUtil.getJsonResponse(199, "已经转发过了");
                }
                return WeixinqingUtil.getJsonResponse(200);
            }
        }
        return WeixinqingUtil.getJsonResponse(0);
    }

    @RequestMapping("/user/comment/{entityType}/{entityId}")
    @ResponseBody
    public String comment(String comment, @PathVariable("entityType") int entityType, @PathVariable("entityId") int entityId) {

        return WeixinqingUtil.getJsonResponse(0);
    }

    @RequestMapping("/user/upvote/{weiboIds}")
    @ResponseBody
    public String upvote(@PathVariable("weiboIds") String weiboIds) {
        int weiboId = WeixinqingUtil.parseWeiboId(weiboIds);
        if (weiboId != -1) {
            int code = weiboService.upvote(hostHolder.getUser().getId(), EntityType.UPVOTE, weiboId);
            return WeixinqingUtil.getJsonResponse(code);
        }
        return WeixinqingUtil.getJsonResponse(0);
    }

    @RequestMapping("/user/collection/{weiboIds}")
    @ResponseBody
    public String collection(@PathVariable("weiboIds") String weiboIds) {
        int weiboId = WeixinqingUtil.parseWeiboId(weiboIds);
        if (weiboId != -1) {
            int code = weiboService.collection(hostHolder.getUser().getId(), EntityType.COLLECTION.getValue(), weiboId) == 1 ? 200 : 0;
            return WeixinqingUtil.getJsonResponse(code);
        }
        return WeixinqingUtil.getJsonResponse(0);
    }
}
