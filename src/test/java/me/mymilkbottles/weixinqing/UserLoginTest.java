package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.service.FocusService;
import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/07/05 10:48.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeixinqingApplication.class)
@WebAppConfiguration
public class UserLoginTest {

    @Autowired
    FocusService focusService;

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Test
    public void userLogin() {
        Map<Integer, Integer> grades = new HashMap<>();
        int sum = userService.userCount();
        for (int i = 33; i < sum; ++i) {

        }
    }
}
