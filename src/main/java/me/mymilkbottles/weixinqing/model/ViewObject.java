package me.mymilkbottles.weixinqing.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/06/19 18:39.
 */
public class ViewObject {

    private Map<String, Object> vo = new HashMap<>();

    public Object get(String key) {
        return vo.get(key);
    }

    public void add(String key, Object value) {
        vo.put(key, value);
    }

    public void set(String key, Object value) {
        vo.put(key, value);
    }


}
