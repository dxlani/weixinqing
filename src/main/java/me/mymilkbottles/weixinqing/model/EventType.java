package me.mymilkbottles.weixinqing.model;

/**
 * Created by Administrator on 2017/06/23 21:37.
 */
public enum EventType {

    LOGIN(1),
    REGISTER(2),
    FIRE_WEIBO(3),
    TRANSMIT(4),
    TRANSMIT_COMMENT(5);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    EventType(int value) {
        this.value = value;
    }
}
