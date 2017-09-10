package me.mymilkbottles.weixinqing.alone.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/06/22 16:12.
 */
public class WeixinqingUtil {

    public static final int ADMIN_ID = 0;

    public static final String TICKET_NAME = "weixinqing_ticket";

    public static String getJsonResponse(int code) {
        JSONObject jsonObject = new JSONObject(1);
        jsonObject.put("code", code);
        return jsonObject.toJSONString();
    }

    public static String getJsonResponse(int code, String msg) {
        JSONObject jsonObject = new JSONObject(2);
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        return jsonObject.toJSONString();
    }

    public static int parseUserId(String userIdS) {
        int userId = -1;
        try {
            userId = Integer.parseInt(userIdS);
        } catch (Exception e) {
        }
        return userId;
    }

    public static int parseWeiboId(String weiboIds) {
        int weiboId = -1;
        try {
            weiboId = Integer.parseInt(weiboIds);
        } catch (Exception e) {
        }
        return weiboId;
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

    public static List<Integer> toIntegerList(List<String> activers) {
        List<Integer> activeInts = new ArrayList<Integer>(activers.size());
        for (String activer : activers) {
            activeInts.add(Integer.parseInt(activer));
        }
        return activeInts;
    }
}
