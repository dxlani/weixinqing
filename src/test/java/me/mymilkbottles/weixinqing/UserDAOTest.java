package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.dao.UserDAO;
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
public class UserDAOTest {

    @Autowired
    UserDAO userMapper;

    @Test
    public void testIsPasswordCorrect() {
        userMapper.isPasswordCorrect(20, "e60d0c3b444a64131fbfd7243747ef8d");
    }
}
