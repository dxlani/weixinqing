package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import me.mymilkbottles.weixinqing.util.LogUtil;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import me.mymilkbottles.weixinqing.util.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.chrono.IsoEra;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Created by Administrator on 2017/06/20 21:07.
 */
@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "from", defaultValue = "", required = false) String from,
                        Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("from", from);
        Object message = session.getAttribute("errorMsg");
        if (message != null) {
            model.addAttribute("errorMsg", message);
            session.removeAttribute("errorMsg");
        }
        Object username = session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            session.removeAttribute("username");
        }
        return "login";
    }

    @RequestMapping(value = "/loginin", method = RequestMethod.POST)
    public String loginIn(@RequestParam(value = "from", defaultValue = "", required = false) String from,
                          String username, String password, String code, HttpSession session,
                          HttpServletResponse response, Model model,
                          HttpServletRequest request) {
        String sessionId = session.getId();
        String message = userService.validateLogin(username, password, code, sessionId);

        if (message != null) {
            session.setAttribute("errorMsg", message);
            session.setAttribute("username", username);
            return "redirect:/login";
        }

        int userId = userService.getUserByMailOrTel(username).getId();

        Cookie cookie = new Cookie("weixinqing_ticket", UUID.randomUUID().toString().replaceAll("-", ""));
        response.addCookie(cookie);

        loginService.insertLoginInfo(cookie.getValue(), userId, request);

        if (from != null && !"".equals(from)) {
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
            LogUtil.error("验证码输出流失败" + e.getMessage());
        }

        jedisAdapter.set(RedisKeyUtil.getVerifyCodeKey(sessionId), vCode.getCode(), 5 * 60 * 1000);
    }

}
