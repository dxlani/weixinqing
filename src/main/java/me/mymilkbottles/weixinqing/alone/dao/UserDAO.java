package me.mymilkbottles.weixinqing.alone.dao;

import me.mymilkbottles.weixinqing.alone.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface UserDAO {

    public static final String TABLE_NAME = " user_table ";

    public static final String FIELD = " username, pwd, salt, mail ";

    public static final String ALL_FIELD = " id, username, pwd, salt, mail,active ";

    @Insert({"insert into " + TABLE_NAME + "(" + FIELD + ") values(#{username}, #{pwd}, #{salt}, #{mail})"})
    public int registerNewUser(User user);

    @Select({"select * from " + TABLE_NAME + " where active = 1 and username = #{username}"})
    public User getUserByUsername(String username);

    @Select({"select * from " + TABLE_NAME + " where active = 1 and mail = #{tel}"})
    public User getUserByMail(String tel);

    @Select({"select * from " + TABLE_NAME + " where active = 1 and id = #{id}"})
    public User getUserById(Integer id);

    @Select({"select count(username) from " + TABLE_NAME + " where username = #{username}"})
    public int isUsernameExist(String username);

    @Select({"select count(mail) from " + TABLE_NAME + " where mail = #{mail}"})
    public int isMailExist(String mail);

    public Boolean isPasswordCorrect(@Param("id") int id, @Param("pwd") String password);

    @Update({"update " + TABLE_NAME + " set active = 1 where id = #{id}"})
    public int active(int id);

    @Select({"select count(id) from " + TABLE_NAME + "where active = 1"})
    public int userCount();
}
