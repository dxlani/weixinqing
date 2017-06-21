package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.UserMapper;
import me.mymilkbottles.weixinqing.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/06/21 23:13.
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

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

    public Boolean isPasswordCorrect(String mail, String tel, String password) {
        return userMapper.isPasswordCorrect(mail, tel, password);
    }

}
