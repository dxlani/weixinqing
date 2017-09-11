package me.mymilkbottles.weixinqing.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/06/19 18:39.
 */
public class ViewObject implements Serializable {

    public ViewObject() {

    }

    public ViewObject(String key, Object value) {
        vo.put(key, value);
    }
    private Map<String, Object> vo = new HashMap<>();

    public Map<String, Object> getVo() {
        return vo;
    }

    public void setVo(Map<String, Object> vo) {
        this.vo = vo;
    }

    public Object get(String key) {
        return vo.get(key);
    }

    public void add(String key, Object value) {
        vo.put(key, value);
    }

    public void set(String key, Object value) {
        vo.put(key, value);
    }


    @Override
    public String toString() {
        return "ViewObject{" +
                "vo=" + vo +
                '}';
    }
}
