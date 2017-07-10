package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/20 22:25.
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String VERIFY_CODE_KEY = "VC";

    private static final String SEND_TEL_MSG_KEY = "STMK";

    private static final String HANDLER_KEY = "HK";

    private static final String ACTIVE_PERSON_KEY = "APK";

    private static final String ACTIVE_PERSON_FEED_KEY = "APF";

    private static final String ACTIVE_FRIEND_FEED_KEY = "AFF";

    private static final String UP_VOTE_KEY = "UV";

    private static final String COLLECTION_KEY = "CK";

    public static String getVerifyCodeKey(String id) {
        return VERIFY_CODE_KEY + SPLIT + id;
    }

    public static String getSendTelMsgKey(String id) {
        return SEND_TEL_MSG_KEY + SPLIT + id;
    }

    public static String getHandlerKey() {
        return HANDLER_KEY;
    }

    public static String getActivePersonKey() {
        return ACTIVE_PERSON_KEY;
    }

    public static String getActivePersonFeedKey() {
        return ACTIVE_PERSON_FEED_KEY;
    }

    public static String getActiveFriendFeedKey() {
        return ACTIVE_FRIEND_FEED_KEY;
    }

    public static String getUpVoteKey(int type, int id) {
        return UP_VOTE_KEY + type + SPLIT + id;
    }

    public static String getCollectionKey(int userId) {
        return COLLECTION_KEY + userId;
    }
}
