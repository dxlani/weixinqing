package me.mymilkbottles.weixinqing.async;


import me.mymilkbottles.weixinqing.util.EntityType;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/06/13 13:32.
 */
public interface Event {

    public Boolean doHandler(EventModel eventModel);

    public List<EntityType> getSupportEntityType();
}
