package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.util.JedisAdapter;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by Administrator on 2017/06/21 11:20.
 */
@Controller
public class RegisterController {

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/getSendTelMsgKey", method = RequestMethod.POST)
    @ResponseBody
    public String getSendTelMsgKey(HttpSession session, HttpServletResponse response,
                                   @RequestParam(value = "code", defaultValue = "", required = false) String code) {
        if (code == null || "".equals(code)) {
            return "";
        }
        code = code.toLowerCase();
        String sessionId = session.getId();
        String redisVerifyCodeKey = RedisKeyUtil.getVerifyCodeKey(sessionId);
        String assertVerifyCode = JedisAdapter.get(redisVerifyCodeKey);
        if (code.equals(assertVerifyCode)) {
            String redisSendTelMsgKey = RedisKeyUtil.getSendTelMsgKey(sessionId);
            String key = UUID.randomUUID().toString().substring(0, 8);
            JedisAdapter.set(redisSendTelMsgKey, key, 5 * 60 * 1000);
            return key;
        }
        return "";
    }


    @RequestMapping(value = "/validateTelMsgKey", method = RequestMethod.POST)
    @ResponseBody
    public String validateTelMsgKey(HttpSession session,
                                    HttpServletResponse response,
                                    @RequestParam(defaultValue = "", required = false) String key) {
        if (key == null || "".equals(key)) {
            return "-1";
        }
        String sessionId = session.getId();
        String redisSendTelMsgKey = RedisKeyUtil.getSendTelMsgKey(sessionId);
        String assertKey = JedisAdapter.get(redisSendTelMsgKey);
        if (key.equals(assertKey)) {
            return "1";
        }
        return "-1";
    }


}
