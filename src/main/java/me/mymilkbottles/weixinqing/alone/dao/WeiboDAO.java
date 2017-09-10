package me.mymilkbottles.weixinqing.alone.dao;

import me.mymilkbottles.weixinqing.alone.model.Weibo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface WeiboDAO {

    public static final String TABLE_NAME = " weibo ";

    public static final String FIELD = " f_time, content, master_id, img ";

    public static final String ALL_FIELD = " id, f_time, content, master_id, img ";

    @Insert({"insert into " + TABLE_NAME + "(" + FIELD + ") values(#{fTime}, #{content}, #{masterId}, #{img})"})
    public int insertWeibo(Weibo weibo);

    @Select({"select * from " + TABLE_NAME + " order by f_time desc limit #{start}, #{end}"})
    public List<Weibo> getWeibo(@Param("start") int start, @Param("end") int end);

    @Select({"select count(id) from " + TABLE_NAME + " where f_time > #{date} and master_id = #{userId}"})
    public int getUserWeiboCountAfterDate(@Param("userId") int userId, @Param("date") Date date);

    @Select({"select * from " + TABLE_NAME + " where id = #{id}"})
    public Weibo getWeiboById(int id);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where id < #{id} order by f_time desc limit #{count}"})
    List<Weibo> getWeiboBeforeId(@Param("id") int id, @Param("count") int count);
}
