package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.Login;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * Created by Administrator on 2017/06/21 23:42.
 */
@Mapper
public interface LoginMapper {

    public static final String TABLE_NAME = " login ";

    public static final String FIELDS = " ticket, expire_date, login_date, detail ";

    public static final String ALL_FIELD = " id, ticket, expire_date, login_date, detail ";

    @Insert({"insert into" + TABLE_NAME + "(" + FIELDS + ")values(#{ticket}, #{expire_date}, #{login_date}, #{detail})"})
    public int insertLoginInfo(Login login);

    public int deleteLoginInfo(Integer id);

}
