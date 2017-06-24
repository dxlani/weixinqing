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

    public EventModel(int eventId, EventType eventType, int producer, int advicer) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.producer = producer;
        this.advicer = advicer;
    }

    public int getProducer() {
        return producer;
    }

    public void setProducer(int producer) {
        this.producer = producer;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getAdvicer() {
        return advicer;
    }

    public void setAdvicer(int advicer) {
        this.advicer = advicer;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getExts() {
        return exts;
    }

    public void setExts(Map<String, Object> exts) {
        this.exts = exts;
    }
}
