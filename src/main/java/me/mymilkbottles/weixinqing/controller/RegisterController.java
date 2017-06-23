package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.RegisterService;
import me.mymilkbottles.weixinqing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

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
    HostHolder hostHolder;

    @Autowired
    LoginService loginService;

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
            if (registerService.registerNewUser(mail, pwd, name) > 0) {
                User user = userService.getUserByUsername(name);
                hostHolder.setUser(user);

                Cookie cookie = new Cookie("weixinqing_ticket", UUID.randomUUID().toString().replaceAll("-", ""));
                cookie.setPath("/");
                response.addCookie(cookie);

                loginService.insertLoginInfo(cookie.getValue(), user.getId(), request);
                model.addAttribute("user", user);
                return "redirect:/index";
            } else {
                result = "注册失败，请您联系客服！";
            }
        }
        model.addAttribute("result", result);
        return "register";
    }
}
