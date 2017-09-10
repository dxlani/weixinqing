package me.mymilkbottles.weixinqing.alone.async;


import me.mymilkbottles.weixinqing.alone.util.EntityType;

import java.util.List;

/**
 * Created by Administrator on 2017/06/13 13:32.
 */
public interface Event {

     Boolean doHandler(EventModel eventModel);

     List<EntityType> getSupportEntityType();
}
