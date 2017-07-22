package me.mymilkbottles.weixinqing.async;


import me.mymilkbottles.weixinqing.model.EventType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/06/13 13:26.
 */
public class EventModel {

    private int eventId;

    private EventType eventType;

    private int producer;

    private int advicer;

    private Map<String, Object> exts = new HashMap<String, Object>();

    public EventModel() {
    }

    public EventModel(int eventId, EventType eventType, int producer, int advicer, Map<String, Object> exts) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.producer = producer;
        this.advicer = advicer;
        this.exts = exts;
    }

    public EventModel(EventType eventType, int producer, Map<String, Object> exts) {
        this.eventType = eventType;
        this.producer = producer;
        this.exts = exts;
    }

    public EventModel(EventType eventType, int producer) {
        this.eventType = eventType;
        this.producer = producer;
    }

    public EventModel addExt(String key, Object obj) {
        this.getExts().put(key, obj);
        return this;
    }

    public int getProducer() {
        return producer;
    }

    public EventModel setProducer(int producer) {
        this.producer = producer;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public EventModel setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getAdvicer() {
        return advicer;
    }

    public EventModel setAdvicer(int advicer) {
        this.advicer = advicer;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Object getExt(String key) {
        return exts.get(key);
    }

    public Map<String, Object> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, Object> exts) {
        this.exts = exts;
        return this;
    }

    public EventModel putExt(String activationMail, Object mail) {
        this.exts = exts;
        return this;
    }
}
