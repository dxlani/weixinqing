package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.Activation;
import me.mymilkbottles.weixinqing.model.Login;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/06/29 14:03.
 */
@Mapper
public interface ActivationMapper {

    public static final String TABLE_NAME = " activation ";

    public static final String FIELDS = " mail,active,expire_time,user_id ";

    public static final String ALL_FIELD = " key,mail,active,expire_time,user_id ";

    @Insert({"insert into" + TABLE_NAME + "(" + ALL_FIELD + ")values(#{key}, #{mail}, #{active}, #{expireTime}, #{userId})"})
    public int insertActivationInfo(Activation activation);

    @Update({"update " + TABLE_NAME + " set expire_time = #{dateTime} where key = #{key}"})
    public int updateExpireTime(@Param("dateTime") Date date, @Param("key") String key);

    @Select({"select expire_time from " + TABLE_NAME + " where key = #{key}"})
    public Date getExpireTime(String key);

    @Select({"select user_id from " + TABLE_NAME + " where key = #{key}"})
    public int getUserId(String key);

    @Select({"select " + ALL_FIELD + "from" + TABLE_NAME + "where key = #{key}"})
    public Activation getActivation(String key);
}
