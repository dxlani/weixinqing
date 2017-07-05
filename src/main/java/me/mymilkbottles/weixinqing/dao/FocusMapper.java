package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.Activation;
import me.mymilkbottles.weixinqing.model.Focus;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/05 10:50.
 */
@Mapper
public interface FocusMapper {

    public static final String TABLE_NAME = " focus ";

    public static final String FIELDS = " master_id, slave_id ";

    public static final String ALL_FIELD = "  id, master_id, slave_id";

    @Insert({"insert into" + TABLE_NAME + "(" + FIELDS + ") values(#{masterId}, #{slaveId})"})
    public int focus(Focus focus);

    @Update({"delete " + TABLE_NAME + " where master_id = #{masterId} and slave_id = ${slaveId}"})
    public int unFocus(@Param("masterId") int masterId, @Param("slaveId") int slaveId);

    @Select({"select count(id) > 0 from " + TABLE_NAME + " where master_id = #{masterId} and slave_id = ${slaveId}"})
    public boolean isFocus(@Param("masterId") int masterId, @Param("slaveId") int slaveId);

}
