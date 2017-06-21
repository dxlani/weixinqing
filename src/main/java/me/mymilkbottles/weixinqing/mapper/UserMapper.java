package me.mymilkbottles.weixinqing.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface UserMapper {

    public static final String TABLE_NAME = "user_table";

    public static final String FIELD = " username, password, salt, mail ";

    @Insert("insert into " + TABLE_NAME + "(" + FIELD + ") values(#{username}, #{password}, #{salt}, #{mail})")
    public void registerNewUser(@Param("username") String username, @Param("password") String password, @Param("salt") String salt, @Param("mail") String mail);

}
