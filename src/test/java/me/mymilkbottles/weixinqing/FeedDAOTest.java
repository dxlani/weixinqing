package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.dao.FeedDAO;
import me.mymilkbottles.weixinqing.dao.UserDAO;
import me.mymilkbottles.weixinqing.model.Feed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/06/21 22:31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeixinqingApplication.class)
@WebAppConfiguration
public class FeedDAOTest {

    @Autowired
    FeedDAO feedDAO;

    @Test
    public void getFeedTest() {
        List<Feed> feeds = feedDAO.getFeed(Arrays.asList(new Integer[]{}), 1, 10);
    }
}
