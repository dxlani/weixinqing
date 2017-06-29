package me.mymilkbottles.weixinqing.controller;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.async.HandlerProducer;
import me.mymilkbottles.weixinqing.model.Activation;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.ActivationService;
import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.RegisterService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2017/06/21 11:20.
 */
@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @Autowired
    UserService userService;

    @Autowired
    HandlerProducer handlerProducer;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginService loginService;

    @Autowired
    ActivationService activationService;

    @RequestMapping("/register")
    public String register(HttpSession session, Model model) {
        return "register";
    }

    @RequestMapping(value = "/register/", method = RequestMethod.POST)
    public String registerAccount(String mail, String pwd, String name, String code,
                                  @CookieValue("JSESSIONID") String sessionId, Model model,
                                  HttpSession session,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String result = registerService.checkRegisterInfo(mail, pwd, name, code, sessionId);
        if (result == null) {
            int returnUserId = registerService.registerNewUser(mail, pwd, name);
            if (returnUserId > 0) {
                String activationKey = UUID.randomUUID().toString().replace("-", "");

                activationService.insertActivationInfo(activationKey, mail, returnUserId);

                String servletPath = request.getServletPath();

                StringBuffer url = request.getRequestURL().append("activation/").append(activationKey);

                String activationLink = url.toString().replace(servletPath, "/");

                EventModel eventModel = new EventModel(EventType.REGISTER, WeixinqingUtil.ADMIN_ID);
                eventModel.putExt("activationMail", mail).putExt("activationKey", activationKey)
                        .putExt("userId", returnUserId).putExt("url", activationLink);

                handlerProducer.produceHandler(eventModel);
                model.addAttribute("url", "/activation/" + activationKey);
            } else {
                model.addAttribute("result", "系统出现故障，请您稍后重试！");
            }
        } else {
            model.addAttribute("result", result);
        }

        return "register";
    }

    @RequestMapping("/resend/{key}")
    @ResponseBody
    public String sendActivationMail(@PathVariable("key") String key, HttpServletRequest request) {

        Activation activation = activationService.getActivation(key);

        if (activation == null) {
            return "链接不合法！";
        }

        String servletPath = request.getServletPath();

        StringBuffer url = request.getRequestURL().append("activation/").append(activation.getKey());

        String activationLink = url.toString().replace(servletPath, "/");

        EventModel eventModel = new EventModel(EventType.REGISTER, WeixinqingUtil.ADMIN_ID);
        eventModel.putExt("activationMail", activation.getMail())
                .putExt("activationKey", activation.getKey()).putExt("url", activationLink);

        handlerProducer.produceHandler(eventModel);

        return "邮件已发送，请您到收件箱中查收！";
    }

    @RequestMapping("/activation/{key}")
    @ResponseBody
    public String activation(@PathVariable("key") String key, HttpServletRequest request) {
        Activation activation = activationService.getActivation(key);
        if (activation == null) {
            return "链接不合法！";
        }

        String servletPath = request.getServletPath();

        if (activationService.getExpireTime(key).before(new Date())) {
            int userId = activation.getUserId();
            if (userService.active(userId) > 0) {

                StringBuffer url = request.getRequestURL().append("index");

                String indexLink = url.toString().replace(servletPath, "/");

                return "恭喜您！邮件激活成功！点击链接进入首页" + indexLink;
            } else {
                StringBuffer url = request.getRequestURL().append("resend/").append(activation.getKey());

                String reSendLink = url.toString().replace(servletPath, "/");
                return "激活失败！点击链接重新发送邮件:" + "<a href='" + reSendLink + "'>" + reSendLink + "</a>";
            }
        }

        StringBuffer url = request.getRequestURL().append("resend/").append(activation.getKey());

        String reSendLink = url.toString().replace(servletPath, "/");

        return "链接已过期！点击链接重新发送邮件:" + "<a href='" + reSendLink + "'>" + reSendLink + "</a>";
    }
}
