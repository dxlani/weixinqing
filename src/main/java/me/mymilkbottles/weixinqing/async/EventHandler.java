package me.mymilkbottles.weixinqing.async;

import me.mymilkbottles.weixinqing.util.EntityType;

/**
 * Created by Administrator on 2017/06/23 15:23.
 */
public interface EventHandler {

    public EntityType getSupportEventType();

    public EventStatus getEventStatus();

    public void setEventStatus();


}
