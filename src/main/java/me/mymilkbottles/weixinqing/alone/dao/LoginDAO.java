package me.mymilkbottles.weixinqing.alone.dao;

import me.mymilkbottles.weixinqing.alone.model.Login;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/06/21 23:42.
 */
@Mapper
public interface LoginDAO {

    public static final String TABLE_NAME = " login ";

    public static final String FIELDS = " ticket, expire_date, login_date, detail,user_id ";

    public static final String ALL_FIELD = " id, ticket, expire_date, login_date, detail, is_delete, user_id ";

    @Insert({"insert into" + TABLE_NAME + "(" + FIELDS + ")values(#{ticket}, #{expireDate}, #{loginDate}, #{detail},#{userId})"})
    public int insertLoginInfo(Login login);

    @Update({"update " + TABLE_NAME + " set is_delete = 1 where  ticket = #{id}"})
    public int deleteLoginInfo(String id);

    @Select({"select expire_date from " + TABLE_NAME + " where ticket = #{id}"})
    public Date getExpireDate(String id);

    @Select({"select is_delete from " + TABLE_NAME + " where ticket = #{id}"})
    public int getStatus(String id);


    @Select({"select user_id from " + TABLE_NAME + " where ticket = #{id}"})
    public int getLoginUser(String id);


    @Select({"select count(id) from " + TABLE_NAME + " where (login_date >= date) and id = #{userId}"})
    public int getUserLoginTimesAfterDate(@Param("userId") int userId, @Param("date") Date date);

    @Update({"update " + TABLE_NAME + " set is_delete = 1 where  user_id = #{id}"})
    int deleteLoginInfoById(Integer id);

    @Update({"update " + TABLE_NAME + " set expire_date = #{time} where  ticket = #{tik}"})
    void updateTicketExpireTime(@Param("tik") String ticket, @Param("time") Date updateTime);
}
