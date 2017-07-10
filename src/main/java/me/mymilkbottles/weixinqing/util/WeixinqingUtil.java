package me.mymilkbottles.weixinqing.util;

import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.configuration.InterceptorConfiguration;

/**
 * Created by Administrator on 2017/06/22 16:12.
 */
public class WeixinqingUtil {

    public static final int ADMIN_ID = 0;

    public static final String TICKET_NAME = "weixinqing_ticket";

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

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }
}
