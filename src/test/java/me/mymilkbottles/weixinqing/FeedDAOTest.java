package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.alone.dao.FeedDAO;
import me.mymilkbottles.weixinqing.alone.model.Feed;
import me.mymilkbottles.weixinqing.alone.service.FocusService;
import me.mymilkbottles.weixinqing.alone.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

    @Autowired
    UserService userService;

    @Autowired
    FocusService focusService;

    @Test
    public void getFeedTest() {
        List<Integer> masters = focusService.getMasterUser(33);
        System.out.println(masters);
        List<Feed> feeds = feedDAO.getFeeds(masters, 0, 10);
        System.out.println(feeds);
    }
}
