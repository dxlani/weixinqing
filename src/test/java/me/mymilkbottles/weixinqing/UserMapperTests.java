package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Administrator on 2017/06/21 22:31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeixinqingApplication.class)
@WebAppConfiguration
public class UserMapperTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testIsPasswordCorrect() {
        userMapper.isPasswordCorrect("javaxz@", null, "e60d0c3b444a64131fbfd7243747ef8d");
    }
}
