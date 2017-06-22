package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.UserMapper;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.dao.JedisAdapter;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
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

    public User getUserByMailOrTel(String mt) {
        return userMapper.getUserByMailOrTel(mt);
    }

    public Boolean isPasswordCorrect(String mail, String tel, String password) {
        return userMapper.isPasswordCorrect(mail, tel, password);
    }

    public String validateLogin(String username, String password, String code, String sessionId) {
        String verifyKey = RedisKeyUtil.getVerifyCodeKey(sessionId);
        String assertCode = jedisAdapter.get(verifyKey);
        if (assertCode != null && assertCode.equals(code)) {
            if (userMapper.isPasswordCorrect(username, username, password)) {
                return null;
            } else {
                return "您的帐号或密码错误，请您重新输入";
            }
        } else {
            return "您输入的验证码错误，请您刷新后重新输入";
        }
    }
}
