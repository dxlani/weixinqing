package me.mymilkbottles.weixinqing.model;

/**
 * Created by Administrator on 2017/06/23 21:37.
 */
public enum EventType {

    LOGIN(1),
    REGISTER(2),
    FIRE_WEIBO(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }
}
