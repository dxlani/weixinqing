package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/23 15:24.
 */
public enum EntityType {

    LOGIN(1),
    REGISTER(2),
    COMMENT(3);

    private int value;

    EntityType() {

    }

    EntityType(int value) {
        this.value = value;
    }

}
