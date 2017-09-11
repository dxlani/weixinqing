package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/07/24 10:44.
 */
public enum MessageType {

    ADVICE(0),
    PRIVATE_MESSAGE(1);

    MessageType() {

    }

    MessageType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
