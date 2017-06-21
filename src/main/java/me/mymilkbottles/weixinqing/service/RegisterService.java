package me.mymilkbottles.weixinqing.service;


import me.mymilkbottles.weixinqing.mapper.UserMapper;
import me.mymilkbottles.weixinqing.util.JedisAdapter;
import me.mymilkbottles.weixinqing.util.Md5Util;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2017/06/21 15:31.
 */
@Service
public class RegisterService {

    @Autowired
    UserMapper userMapper;

    public String checkRegisterInfo(String mail, String pwd, String name, String code, String id) {
        if (mail.indexOf('@') == -1) {
            return "请输入合法的邮箱账户！";
        }
        if (name == null || "".equals(name)) {
            return "请输入您的用户名昵称！";
        }
        if (name.length() < 6) {
            return "您的用户名昵称长度过短，请您重新输入！";
        }
        if (pwd == null || "".equals(pwd)) {
            return "请输入您的密码！";
        }
        if (pwd.length() < 6) {
            return "您的密码长度过短，请您重新输入！";
        }
        if (code == null || "".equals(code)) {
            return "请输入验证码！";
        }
        String codeKey = RedisKeyUtil.getVerifyCodeKey(id);
        String assertCode = JedisAdapter.get(codeKey);
        if (assertCode == null) {
            return "您的验证码已过期，请您刷新验证码后重新输入！";
        }
        if (!assertCode.equals(code)) {
            return "您的验证码输入错误，请您重新输入！";
        }
        return null;
    }

    public void registerNewUser(String mail, String pwd, String name) {
        String salt = UUID.randomUUID().toString().substring(0, 6);
        pwd = Md5Util.getMD5(pwd + salt);
        userMapper.registerNewUser(name, pwd, salt, mail);
    }
}
