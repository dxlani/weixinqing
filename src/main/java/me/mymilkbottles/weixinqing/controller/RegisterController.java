package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.dao.UserMapper;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.service.RegisterService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.JedisAdapter;
import me.mymilkbottles.weixinqing.util.LogUtil;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/register")
    public String register(HttpSession session, Model model) {
        Object result = session.getAttribute("result");
        if (result != null) {
            model.addAttribute("result", result);
            session.removeAttribute("result");
        }
        return "register";
    }

    @RequestMapping(value = "/registeraccount", method = RequestMethod.POST)
    public String registerAccount(String mail, String pwd, String name, String code,
                                  @CookieValue("JSESSIONID") String sessionId,
                                  HttpSession session) {
        String result = registerService.checkRegisterInfo(mail, pwd, name, code, sessionId);
        if (result == null) {
            if (registerService.registerNewUser(mail, pwd, name) > 0) {
                hostHolder.setUser(userService.getUserByUsername(name));
                return "redirect:/index";
            } else {
                result = "注册失败，请您联系客服！";
            }
        }
        session.setAttribute("result", result);
        return "redirect:/register";
    }
}
