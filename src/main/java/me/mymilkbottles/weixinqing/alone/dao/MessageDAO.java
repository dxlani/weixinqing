package me.mymilkbottles.weixinqing.alone.dao;

import me.mymilkbottles.weixinqing.alone.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface MessageDAO {

    public static final String TABLE_NAME = " msg ";

    public static final String FIELD = " content, stime, producer,  advicer, is_delete, is_read, msg_type ";

    public static final String ALL_FIELD = " id, content, stime, producer,  advicer, is_delete, is_read, msg_type ";

    @Insert({"insert into " + TABLE_NAME + "(" + FIELD + ") values (#{content}, #{stime}, #{producer}, #{advicer}, #{isDelete}, #{isRead}, #{msgType})"})
    public int insertMessage(Message message);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where id = #{id} and (is_delete != 1 or is_delete is not null)"})
    public Message getMessageById(@Param("id") int id);

    @Select({"select count(id) from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null)1 and is_read = 0"})
    public int getUserUnreadMessageCount(int id);

    @Select({"select count(id) from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null)"})
    public int getUserMessageCount(int id);

    @Select({"select count(id) from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null) and is_read = 0 and msg_type = #{type}"})
    public int getUserUnreadTypeMessageCount(@Param("id") int id, @Param("type") int type);

    @Select({"select count(id) from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null) and msg_type = #{type}"})
    public int getUserTypeMessageCount(@Param("id") int id, @Param("type") int type);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null) and is_read = 0 order by stime desc limit #{s}, #{e}"})
    public List<Message> getUserUnreadMessage(@Param("id") int id, @Param("s") int start, @Param("e") int end);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null) order by stime desc limit #{s}, #{e}"})
    public List<Message> getUserMessage(@Param("id") int id, @Param("s") int start, @Param("e") int end);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where advicer = #{id} and is_delete != 1 and is_read = 0 and msg_type = #{type} order by stime desc limit #{s}, #{e}"})
    public List<Message> getUserUnreadTypeMessage(@Param("id") int id, @Param("type") int type, @Param("s") int start, @Param("e") int end);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where advicer = #{id} and (is_delete != 1 or is_delete is not null) and msg_type = #{type} order by stime desc limit #{s}, #{e}"})
    public List<Message> getUserTypeMessage(@Param("id") int id, @Param("type") int type, @Param("s") int start, @Param("e") int end);

}
