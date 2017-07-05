package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.Weibo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface WeiboMapper {

    public static final String TABLE_NAME = " weibo ";

    public static final String FIELD = " f_time, content, master_id, img ";

    public static final String ALL_FIELD = " id, f_time, content, master_id, img ";

    @Insert({"insert into " + TABLE_NAME + "(" + FIELD + ") values(#{fTime}, #{content}, #{masterId}, #{img})"})
    public int insertWeibo(Weibo weibo);

    @Select({"select * from " + TABLE_NAME + " order by f_time desc limit #{start}, #{end}"})
    public List<Weibo> getWeibo(@Param("start") int start, @Param("end") int end);

    @Select({"select * from " + TABLE_NAME + " where id < #{id} order by f_time desc limit #{start}, #{end}"})
    public List<Weibo> getWeiboBeforeId(@Param("start") int start, @Param("end") int end, @Param("id") int id);
}
