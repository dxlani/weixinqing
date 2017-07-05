package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.UserDAO;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.util.Md5Util;
import me.mymilkbottles.weixinqing.util.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/06/21 23:13.
 */
@Service
public class UserService {

    @Autowired
    UserDAO userMapper;

    @Autowired
    JedisDAO jedisAdapter;

    @Autowired
    LoginService loginService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    FocusService focusService;

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

    public boolean isUserActive(int userId) {
        String activePersonKey = RedisKeyUtil.getActivePersonKey();
        return jedisAdapter.sismember(activePersonKey, String.valueOf(userId));
    }

    public void updateActiveUser() {
        int sum = userCount();
        Map<Integer, Integer> grades = new HashMap<>(sum);

        User user = null;

        int count = sum / 200, min = Integer.MAX_VALUE;
        for (int i = 1; i <= sum; ++i) {
            user = getUserById(i);
            if (user != null) {
                int userGrades = 0;
                Date date = new Date(new Date().getTime() - (7 * 24 * 60 * 60 * 1000));
                int times = loginService.getUserLoginTimesAfterDate(i, date);

                if (times >= 7) {
                    times = 7;
                }

                userGrades += times * 30;

                int weiboCount = weiboService.getUserWeiboCountAfterDate(i, date);
                userGrades += weiboCount * 10;

                int commentsCount = commentsService.getUserCommentsCountAfterDate(i, date);
                userGrades += commentsCount;

                int slaveCount = focusService.slaveCount(i);
                userGrades += slaveCount;

                if (count > 0) {
                    min = Math.min(min, userGrades);
                    grades.put(userGrades, i);
                    --count;
                } else {
                    if (min < userGrades) {
                        grades.remove(min);
                        min = userGrades;
                        grades.put(userGrades, i);
                    }
                }
            }
        }

        String activePersonKey = RedisKeyUtil.getActivePersonKey();
        Set<String> oldActiveUsers = jedisAdapter.smembers(activePersonKey);

        Collection<Integer> values = grades.values();

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedis.multi();
        for (String oldActiveUser : oldActiveUsers) {
            jedis.srem(activePersonKey, oldActiveUser);
        }
        for (Integer userId : values) {
            String userIdStr = String.valueOf(userId);
            jedis.sadd(activePersonKey, userIdStr);
        }
        tx.exec();
        try {
            if (tx != null) {
                tx.close();
            }
        } catch (IOException e) {
            log.error("jedis Transaction关闭失败" + e.getMessage());
        }
        jedis.close();
    }
}
