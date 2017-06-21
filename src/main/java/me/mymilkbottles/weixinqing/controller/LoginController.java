package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.util.JedisAdapter;
import me.mymilkbottles.weixinqing.util.LogUtil;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import me.mymilkbottles.weixinqing.util.VerifyCodeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Administrator on 2017/06/20 21:07.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {

        return "login";
    }


    @RequestMapping(value = "/verifycode")
    public void getVerifyCode(HttpServletResponse response,
                              @CookieValue("JSESSIONID") String sessionId) {

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

        JedisAdapter.set(RedisKeyUtil.getCerifyCodeKey(sessionId), vCode.getCode(), 5 * 60 * 1000);
    }

}
