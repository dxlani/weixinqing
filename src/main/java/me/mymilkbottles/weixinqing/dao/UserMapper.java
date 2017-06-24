package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface UserMapper {

    public static final String TABLE_NAME = "user_table";

    public static final String FIELD = " username, pwd, salt, mails ";

    public static final String ALL_FIELD = " id, username, pwd, salt, mails, tel ";

    @Insert({"insert into " + TABLE_NAME + "(" + FIELD + ") values(#{username}, #{pwd}, #{salt}, #{mails})"})
    public int registerNewUser(User user);

    @Select({"select * from " + TABLE_NAME + " where username = #{username}"})
    public User getUserByUsername(String username);

    @Select({"select * from " + TABLE_NAME + " where mails = #{mt} or tel = #{mt}"})
    public User getUserByMailOrTel(String mt);

    @Select({"select * from " + TABLE_NAME + " where id = #{id}"})
    public User getUserById(Integer id);

    @Select({"select * from " + TABLE_NAME + " where tel = #{tel}"})
    public User getUserByTel(String tel);

    @Select({"select count(username) from " + TABLE_NAME + " where username = #{username}"})
    public int isUsernameExist(String username);

    @Select({"select count(tel) from " + TABLE_NAME + " where tel = #{tel}"})
    public int isTelExist(String tel);

    @Select({"select count(mails) from " + TABLE_NAME + " where mails = #{mails}"})
    public int isMailExist(String mail);

    public Boolean isPasswordCorrect(
            @Param("id") int id, @Param("pwd") String password);

}
