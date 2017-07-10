package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.dao.UserDAO;
import me.mymilkbottles.weixinqing.service.FocusService;
import me.mymilkbottles.weixinqing.service.UserActiveService;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.EntityType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

/**
 * Created by Administrator on 2017/07/05 10:48.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeixinqingApplication.class)
@WebAppConfiguration
public class UserActiveServiceTest {

    @Autowired
    FocusService focusService;

    @Autowired
    UserService userService;

    @Autowired
    UserActiveService userActiveService;

    @Test
    public void userActiveServiceTest() {
        String types = String.valueOf(EntityType.WEIBO.getValue());
        try {
            userActiveService.upVote(types, "33", 33);
        } catch (Exception e) {
            Assert.fail("upVote exception  " + e.getMessage());
        }

        try {
            userActiveService.collection(types, "18", 33);
        } catch (Exception e) {
            Assert.fail("collection exception  " + e.getMessage());
        }

        try {
            userActiveService.comments("33", "18", "好警示啊");
        } catch (Exception e) {
            Assert.fail("comments exception  " + e.getMessage());
        }

        try {
            userActiveService.transmit("18", 33);
        } catch (Exception e) {
            Assert.fail("transmit exception  " + e.getMessage());
        }

        try {
            userActiveService.downVote(types, "18", 33);
        } catch (Exception e) {
            Assert.fail("downVote exception  " + e.getMessage());
        }
    }
}
