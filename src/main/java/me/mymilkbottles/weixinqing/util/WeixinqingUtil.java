package me.mymilkbottles.weixinqing.util;

import me.mymilkbottles.weixinqing.configuration.InterceptorConfiguration;

/**
 * Created by Administrator on 2017/06/22 16:12.
 */
public class WeixinqingUtil {

    public static final int ADMIN_ID = 0;

    public static int parseUserId(String userIdS) {
        int userId = -1;
        try {
            userId = Integer.parseInt(userIdS);
        } catch (Exception e) {
        }
        return userId;
    }

    public static int parsePage(String pageS) {
        int page = 1;
        try {
            page = Integer.parseInt(pageS);
        } catch (Exception e) {
        }
        return page;
    }
}
