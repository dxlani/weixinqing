package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.Activation;

import me.mymilkbottles.weixinqing.model.EventModel;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.*;

import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    HostHolder hostHolder;

    @Autowired
    AsyncEventService asyncEventService;

    @Autowired
    LoginService loginService;

    @Autowired
    ActivationService activationService;

    @Value("${weixinqing.domain}")
    String weixinqingDomain;

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
                                  HttpSession session) {
        String result = registerService.checkRegisterInfo(mail, pwd, name, code, session.getId());
        if (result == null) {
            int returnUserId = registerService.registerNewUser(mail, pwd, name);
            if (returnUserId > 0) {
                String activationKey = UUID.randomUUID().toString().replace("-", "");

                String sendKey = UUID.randomUUID().toString().replace("-", "");

                activationService.insertActivationInfo(activationKey, mail, returnUserId, sendKey);

                StringBuilder activationLink = new StringBuilder(weixinqingDomain).append("/activation/").append(activationKey);

                log.info("registerUser activationLink = " + activationLink);

                EventModel eventModel = new EventModel(EntityType.REGISTER, WeixinqingUtil.ADMIN_ID);
                eventModel.addExt("activationMail", mail).addExt("activationKey", activationKey)
                        .addExt("url", activationLink);

                asyncEventService.sendEvent(eventModel);

                StringBuilder sendUrl = new StringBuilder(weixinqingDomain).append("/resend/").append(sendKey);

                session.setAttribute("url", sendUrl.toString());

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
    public String resendActivationMail(@PathVariable("key") String sendKey ) {

        Activation activation = activationService.getActivationBySendKey(sendKey);

        if (activation == null) {
            return "发送失败！该激活码不存在！";
        }

        StringBuilder activationLink = new StringBuilder(weixinqingDomain).append("/activation/").append(activation.getActivationKey());

        log.info("resend activationLink = " + activationLink);
        EventModel eventModel = new EventModel(EntityType.REGISTER, WeixinqingUtil.ADMIN_ID);
        eventModel.addExt("activationMail", activation.getMail()).addExt("activationKey", activation.getActivationKey())
                .addExt("url", activationLink.toString());

        asyncEventService.sendEvent(eventModel);

        String resultString = "邮件已发送，请您到收件箱中查收！还没收到？<a href='" + weixinqingDomain + "/resend/" + sendKey + "'>重新发送</a>";

        return resultString;
    }

    @RequestMapping("/activation/{key}")
    @ResponseBody
    public String activation(@PathVariable("key") String activeKey, HttpServletRequest request,
                             HttpServletResponse response) {
        Activation activation = activationService.getActivationByActivationKey(activeKey);
        if (activation == null) {
            return "激活失败！该激活码不存在！";
        }
        if (activationService.getExpireTime(activeKey).after(new Date())) {
            int userId = activation.getUserId();
            if (userService.active(userId) > 0) {


                Cookie cookie = new Cookie("weixinqing_ticket", UUID.randomUUID().toString().replaceAll("-", ""));
                cookie.setPath("/");
                response.addCookie(cookie);

                User user = userService.getUserById(userId);
                hostHolder.setUser(user);

                loginService.insertLoginInfo(cookie.getValue(), userId, request);

                return "恭喜您！邮件激活成功！点击链接进入首页<a href=" + weixinqingDomain + ">" + weixinqingDomain + "</a>";
            } else {
                String reSendLink = new StringBuilder(weixinqingDomain).append("/resend/").append(activation.getSendKey()).toString();
                return "激活失败！点击链接重新发送邮件:" + "<a href='" + reSendLink + "'>" + reSendLink + "</a>";
            }
        }
        String reSendLink = new StringBuilder(weixinqingDomain).append("/resend/").append(activation.getSendKey()).toString();
        return "链接已过期！点击链接重新发送邮件:" + "<a href='" + reSendLink + "'>" + reSendLink + "</a>";
    }
}
