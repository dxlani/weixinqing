package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/20 22:25.
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String VERIFY_CODE_KEY = "VC";

    private static final String SEND_TEL_MSG_KEY = "STMK";

    public static String getVerifyCodeKey(String id) {
        return VERIFY_CODE_KEY + SPLIT + id;
    }

    public static String getSendTelMsgKey(String id) {
        return SEND_TEL_MSG_KEY + SPLIT + id;
    }

}
