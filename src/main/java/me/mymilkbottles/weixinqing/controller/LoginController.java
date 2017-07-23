package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import me.mymilkbottles.weixinqing.util.VerifyCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/06/20 21:07.
 */
@Controller
public class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    JedisDAO jedisAdapter;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "from", defaultValue = "", required = false) String from,
                        @RequestParam(value = "username", defaultValue = "", required = false) String username,
                        @RequestParam(value = "message", defaultValue = "", required = false) String message,
                        Model model, HttpSession session, HttpServletRequest request) {
        if (StringUtils.isNotBlank(from)) {
            model.addAttribute("from", from);
        }
        if (StringUtils.isNotBlank(username)) {
            model.addAttribute("username", username);
        }
        if (StringUtils.isNotBlank(message)) {
            model.addAttribute("errorMsg", message);
        }
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        User user = hostHolder.getUser();

        if (user != null) {
            loginService.deleteLoginInfoById(user.getId());
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public String loginIn(@RequestParam(value = "from", defaultValue = "", required = false) String from,
                          String username, String password, String code, HttpSession session,
                          HttpServletResponse response, Model model,
                          HttpServletRequest request) {

//        log.info("/login/" + "user=" + hostHolder.getUser());

        String sessionId = session.getId();
        String message = userService.validateLogin(username, password, code, sessionId);

        if (message != null) {
            model.addAttribute("username", username);
            model.addAttribute("errorMsg", message);
            return "login";
        }

        User user = userService.getUserByMail(username);

        Cookie cookie = new Cookie("weixinqing_ticket", UUID.randomUUID().toString().replaceAll("-", ""));
        cookie.setPath("/");
        response.addCookie(cookie);

//        log.info("/login/" + "addCookie=" + cookie.getValue() + "cookie path=" + cookie.getPath());
        hostHolder.setUser(user);

        loginService.insertLoginInfo(cookie.getValue(), user.getId(), request);

        if (StringUtils.isNotBlank(from)) {
            //检查from是否为本站合法地址
            return "redirect:" + from;
        }
        return "redirect:/index";
    }


    @RequestMapping(value = "/verifycode")
    public void getVerifyCode(HttpServletResponse response,
                              HttpSession session) {

        String sessionId = session.getId();

        //设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");

        //禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        VerifyCodeUtil vCode = new VerifyCodeUtil(80, 30, 4, 20);
        try {
            vCode.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码输出流失败" + e.getMessage());
        }

        jedisAdapter.set(RedisKeyUtil.getVerifyCodeKey(sessionId), vCode.getCode(), 5 * 60 * 1000);
    }

}
