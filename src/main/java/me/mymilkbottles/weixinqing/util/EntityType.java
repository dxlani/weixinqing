package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/23 15:24.
 */
public enum EntityType {

    LOGIN(1),
    REGISTER(2),
    COMMENT(3),
    TRANSMIT(4),
    UPVOTE(5),
    FIRE_WEIBO(6),
    TRANSMIT_COMMENT(7),
    COLLECTION(8),
    WEIBO(9),
    INDEX_CONTENT(10),
    INDEX_USERNAME(11);

    private int value;

    EntityType() {
    }

    EntityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
