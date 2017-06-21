package me.mymilkbottles.weixinqing.util;

import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2017/06/20 23:31.
 */
public class LogUtil {

    private static final Logger log = Logger.getLogger(LogUtil.class);


    public static void info(String message) {
        log.info(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void debug(String message) {
        log.debug(message);
    }

}
