package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.async.HandlerCustomer;
import me.mymilkbottles.weixinqing.dao.UserMapper;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import me.mymilkbottles.weixinqing.util.Md5Util;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/06/21 23:13.
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger log = Logger.getLogger(UserService.class);

    public int registerNewUser(User user) {
        return userMapper.registerNewUser(user);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    public int isUsernameExist(String username) {
        return userMapper.isUsernameExist(username);
    }

    public int isMailExist(String mail) {
        return userMapper.isMailExist(mail);
    }

    public User getUserByMail(String tel) {
        return userMapper.getUserByMail(tel);
    }

    public Boolean isPasswordCorrect(int id, String password) {
        return userMapper.isPasswordCorrect(id, password);
    }

    public int userCount() {
        return userMapper.userCount();
    }

    public String validateLogin(String username, String password, String code, String sessionId) {
        String verifyKey = RedisKeyUtil.getVerifyCodeKey(sessionId);
        String assertCode = jedisAdapter.get(verifyKey);
        if (assertCode != null && assertCode.equals(code)) {
            User user = userMapper.getUserByMail(username);
            if (user == null) {
                return "帐号不存在！";
            }
            password = Md5Util.getMD5(password + user.getSalt());
            if (userMapper.isPasswordCorrect(user.getId(), password)) {
                return null;
            }
            return "您的帐号或密码错误，请您重新输入";
        }
        return "您输入的验证码错误，请您刷新后重新输入";
    }

    public int active(int id) {
        return userMapper.active(id);
    }
}
