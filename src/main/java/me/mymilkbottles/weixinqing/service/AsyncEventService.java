package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.model.EventModel;

public interface AsyncEventService {
      boolean sendEvent(EventModel eventModel);
}
