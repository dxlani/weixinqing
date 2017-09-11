package me.mymilkbottles.weixinqing;

import me.mymilkbottles.weixinqing.service.FocusService;
import me.mymilkbottles.weixinqing.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Administrator on 2017/07/05 10:48.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeixinqingApplication.class)
@WebAppConfiguration
public class AddFocusTest {

    @Autowired
    FocusService focusService;

    @Autowired
    UserService userService;

    @Test
    public void addFocus() {
        int sum = userService.userCount();

        for (int i = 34; i < sum; ++i) {
            if (!focusService.isFocus(i, 33)) {
                focusService.focus(i, 33);
            }
        }
//        for (int i = 33; i < sum; ++i) {
//            if (random.nextInt(5000) <= 2) {
//
//            } else {
//                for (int j = 0; j < 3; ++j) {
//                    int temp;
//                    while ((temp = random.nextInt(sum)) >= 33 && temp != i) {
//                        break;
//                    }
//                    focusService.focus(temp, i);
//                    Assert.assertEquals(focusService.isFocus(temp, i), true);
//                }
//            }
//        }
    }
}
