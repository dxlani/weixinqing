package me.mymilkbottles.weixinqing.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getNowDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public static String getNowDate(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

}
