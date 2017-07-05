package me.mymilkbottles.weixinqing.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/05 17:12.
 */
public class Comments {

    private long id;

    private Date fTime;

    private String content;

    private int masterId;

    private int weiboId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(int weiboId) {
        this.weiboId = weiboId;
    }
}
