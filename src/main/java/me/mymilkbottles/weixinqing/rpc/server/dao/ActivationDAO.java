package me.mymilkbottles.weixinqing.rpc.server.dao;

import me.mymilkbottles.weixinqing.alone.model.Activation;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/06/29 14:03.
 */
@Mapper
public interface ActivationDAO {

    public static final String TABLE_NAME = " activation ";

    public static final String FIELDS = " mail,active,expire_time,user_id, send_key ";

    public static final String ALL_FIELD = " activation_key,mail,active,expire_time,user_id, send_key ";

    @Insert({"insert into" + TABLE_NAME + "(" + ALL_FIELD + ")values(#{activationKey}, #{mail}, #{active}, #{expireTime}, #{userId}, #{sendKey})"})
    public int insertActivationInfo(Activation activation);

    @Update({"update " + TABLE_NAME + " set expire_time = #{dateTime} where activation_key = #{activationKey}"})
    public int updateExpireTime(@Param("dateTime") Date date, @Param("activationKey") String activationKey);

    @Select({"select expire_time from " + TABLE_NAME + " where activation_key = #{activationKey}"})
    public Date getExpireTime(String activationKey);

    @Select({"select send_key from " + TABLE_NAME + " where activation_key = #{activationKey}"})
    public String getSendKey(String activationKey);

    @Select({"select user_id from " + TABLE_NAME + " where activation_key = #{activationKey}"})
    public int getUserId(String activationKey);

    @Select({"select " + ALL_FIELD + "from" + TABLE_NAME + "where activation_key = #{key}"})
    Activation getActivationByActivationKey(String key);

    @Select({"select " + ALL_FIELD + "from" + TABLE_NAME + "where send_key = #{key}"})
    Activation getActivationBySendKey(String key);
}
