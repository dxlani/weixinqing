package me.mymilkbottles.weixinqing.rpc.server.service;


import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.dao.UserDAO;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.util.Md5Util;
import me.mymilkbottles.weixinqing.alone.util.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2017/06/21 15:31.
 */
@Service
public class RegisterService {

    @Autowired
    UserDAO userMapper;

    @Autowired
    JedisDAO jedisAdapter;

    private static final Logger log = Logger.getLogger(RegisterService.class);

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
        String assertCode = jedisAdapter.get(codeKey);
        if (assertCode == null) {
            return "您的验证码已过期，请您刷新验证码后重新输入！";
        }
        if (!assertCode.equals(code)) {
            return "您的验证码输入错误，请您重新输入！";
        }

        if (userMapper.isUsernameExist(name) > 0) {
            return "用户名已存在！";
        }

        if (userMapper.isMailExist(mail) > 0) {
            return "邮箱已经注册，请您直接登录！";
        }

        return null;
    }

    public int registerNewUser(String mail, String pwd, String name) {

        String salt = UUID.randomUUID().toString().substring(0, 6);
        pwd = Md5Util.getMD5(pwd + salt);
        User user = new User();
        user.setMail(mail);
        user.setPwd(pwd);
        user.setSalt(salt);
        user.setUsername(name);

        if (userMapper.registerNewUser(user) > 0) {
            return user.getId();
        }
        return -1;
    }
}
