package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/20 22:25.
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String VERIFY_CODE_KEY = "VC";

    public static String getCerifyCodeKey(String ticket) {
        return VERIFY_CODE_KEY + SPLIT + ticket;
    }
}
