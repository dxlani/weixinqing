package me.mymilkbottles.weixinqing.rpc.server.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/24 10:05.
 */
public class Message {
    private long id;

    private String content;

    private Date stime;

    private int producer;

    private int advicer;

    private int isDelete;

    private int isRead;

    private int msgType;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStime() {
        return stime;
    }

    public void setStime(Date stime) {
        this.stime = stime;
    }

    public int getProducer() {
        return producer;
    }

    public void setProducer(int producer) {
        this.producer = producer;
    }

    public int getAdvicer() {
        return advicer;
    }

    public void setAdvicer(int advicer) {
        this.advicer = advicer;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
