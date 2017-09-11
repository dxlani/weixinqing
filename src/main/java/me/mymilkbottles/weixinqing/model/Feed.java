package me.mymilkbottles.weixinqing.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/05 19:38.
 */
public class Feed {

    private long id;

    private int userId;

    private int type;

    private int weiboId;

    private int extsId;

    private Date fTime;

    private int isDelete;

    public Feed() {

    }

    public Feed(int userId, int type, int weiboId, int extsId, Date fTime, int isDelete) {
        this.userId = userId;
        this.type = type;
        this.weiboId = weiboId;
        this.extsId = extsId;
        this.fTime = fTime;
        this.isDelete = isDelete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(int weiboId) {
        this.weiboId = weiboId;
    }

    public int getExtsId() {
        return extsId;
    }

    public void setExtsId(int extsId) {
        this.extsId = extsId;
    }

    public Date getfTime() {
        return fTime;
    }

    public void setfTime(Date fTime) {
        this.fTime = fTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", weiboId=" + weiboId +
                ", extsId=" + extsId +
                ", fTime=" + fTime +
                ", isDelete=" + isDelete +
                '}';
    }
}
