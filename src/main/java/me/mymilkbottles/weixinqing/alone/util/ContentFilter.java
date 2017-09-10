package me.mymilkbottles.weixinqing.alone.util;

import org.springframework.web.util.HtmlUtils;

/**
 * Created by Administrator on 2017/07/24 20:16.
 */
public class ContentFilter {
    public static String filter(String origin) {
        origin = SensitiveWordFilterUtil.filter(origin);
        HtmlUtils.htmlEscape(origin);
        return origin;
    }
}
