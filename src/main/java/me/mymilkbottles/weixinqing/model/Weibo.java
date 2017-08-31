package me.mymilkbottles.weixinqing.model;

import java.util.*;

/**
 * Created by Administrator on 2017/07/02 20:10.
 */
public class Weibo {

    private int id;

    private Date fTime;

    private String content;

    private int masterId;

    private String img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getfTime() {
        return fTime;
    }

    public void setfTime(Date fTime) {
        this.fTime = fTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    @Override
    public String toString() {
        return "Weibo{" +
                "id=" + id +
                ", fTime=" + fTime +
                ", content='" + content + '\'' +
                ", masterId=" + masterId +
                ", img='" + img + '\'' +
                '}';
    }
}
