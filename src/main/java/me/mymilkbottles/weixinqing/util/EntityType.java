package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/23 15:24.
 */
public enum EntityType {

    LOGIN(1),
    REGISTER(2),
    COMMENT(3),
    FORWARD(4),
    UPVOTE(5),
    FIRE_WEIBO(6),
    FORWARD_COMMENTS(7);

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
