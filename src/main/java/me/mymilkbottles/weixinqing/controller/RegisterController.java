package me.mymilkbottles.weixinqing.controller;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.async.HandlerProducer;
import me.mymilkbottles.weixinqing.async.handler.LoginHandler;
import me.mymilkbottles.weixinqing.model.Activation;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.ActivationService;
import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.RegisterService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.apache.log4j.Logger;
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

    private static final Logger log = Logger.getLogger(RegisterController.class);

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
        Object result = session.getAttribute("result");
        if (result != null) {
            model.addAttribute("result", result);
            session.removeAttribute("result");
        }
        Object url = session.getAttribute("url");
        if (url != null) {
            model.addAttribute("url", url);
            session.removeAttribute("url");
        }
        return "register";
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String registerAccount(String mail, String pwd, String name, String code,
                                  HttpSession session,
                                  HttpServletRequest request) {
        String result = registerService.checkRegisterInfo(mail, pwd, name, code, session.getId());
        if (result == null) {
            int returnUserId = registerService.registerNewUser(mail, pwd, name);
            if (returnUserId > 0) {
                String activationKey = UUID.randomUUID().toString().replace("-", "");

                String sendKey = UUID.randomUUID().toString().replace("-", "");

                activationService.insertActivationInfo(activationKey, mail, returnUserId, sendKey);

                String servletPath = request.getServletPath();

                StringBuffer activationUrl = request.getRequestURL().append("activation/").append(activationKey);

                String activationLink = activationUrl.toString().replace(servletPath, "/");

                EventModel eventModel = new EventModel(EntityType.REGISTER, WeixinqingUtil.ADMIN_ID);
                eventModel.addExt("activationMail", mail).addExt("activationKey", activationKey)
                        .addExt("url", activationLink);

                handlerProducer.produceHandler(eventModel);

                StringBuffer sendUrl = request.getRequestURL().append("resend/").append(sendKey);

                String sendLink = sendUrl.toString().replace(servletPath, "/");

                session.setAttribute("url", sendLink);

            } else {
                session.setAttribute("result", "系统出现故障，请您稍后重试！");
            }
        } else {
            session.setAttribute("result", result);
        }
        return "redirect:/register";
    }

    @RequestMapping("/resend/{key}")
    @ResponseBody
    public String sendActivationMail(@PathVariable("key") String sendKey, HttpServletRequest request) {

        Activation activation = activationService.getActivationBySendKey(sendKey);

        if (activation == null) {
            return "发送失败！";
        }

        String servletPath = request.getServletPath();

        StringBuffer url = request.getRequestURL().append("activation/").append(activation.getActivationKey());

        String activationLink = url.toString().replace(servletPath, "/");

        EventModel eventModel = new EventModel(EntityType.REGISTER, WeixinqingUtil.ADMIN_ID);
        eventModel.addExt("activationMail", activation.getMail()).addExt("activationKey", activation.getActivationKey())
                .addExt("url", activationLink);

        handlerProducer.produceHandler(eventModel);

        String resultString = "邮件已发送，请您到收件箱中查收！还没收到？<a href='/resend/" + sendKey + "'>重新发送</a>";

        return resultString;
    }

    @RequestMapping("/activation/{key}")
    @ResponseBody
    public String activation(@PathVariable("key") String activeKey, HttpServletRequest request,
                             HttpServletResponse response) {
        Activation activation = activationService.getActivationByActivationKey(activeKey);
        if (activation == null) {
            return "激活失败！";
        }

        String servletPath = request.getServletPath();

        if (activationService.getExpireTime(activeKey).after(new Date())) {
            int userId = activation.getUserId();
            if (userService.active(userId) > 0) {

                StringBuffer url = request.getRequestURL().append("index");

                String indexLink = url.toString().replace(servletPath, "/");

                Cookie cookie = new Cookie("weixinqing_ticket", UUID.randomUUID().toString().replaceAll("-", ""));
                cookie.setPath("/");
                response.addCookie(cookie);

                User user = userService.getUserById(userId);
                hostHolder.setUser(user);

                loginService.insertLoginInfo(cookie.getValue(), userId, request);

                return "恭喜您！邮件激活成功！点击链接进入首页<a href=" + indexLink + ">" + indexLink + "</a>";
            } else {
                StringBuffer url = request.getRequestURL().append("resend/").append(activation.getSendKey());

                String reSendLink = url.toString().replace(servletPath, "/");

                return "激活失败！点击链接重新发送邮件:" + "<a href='" + reSendLink + "'>" + reSendLink + "</a>";
            }
        }

        StringBuffer url = request.getRequestURL().append("resend/").append(activation.getSendKey());

        String reSendLink = url.toString().replace(servletPath, "/");

        return "链接已过期！点击链接重新发送邮件:" + "<a href='" + reSendLink + "'>" + reSendLink + "</a>";
    }
}
