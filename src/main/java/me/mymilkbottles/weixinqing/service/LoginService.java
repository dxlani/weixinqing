package me.mymilkbottles.weixinqing.service;

/**
 * Created by Administrator on 2017/06/22 10:15.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.mymilkbottles.weixinqing.dao.LoginMapper;
import me.mymilkbottles.weixinqing.model.Login;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LoginService {

    @Autowired
    LoginMapper loginMapper;

    public int deleteLoginInfo(String id) {
        return loginMapper.deleteLoginInfo(id);
    }

    public Date getExpireDate(String id) {
        return loginMapper.getExpireDate(id);
    }

    public int getStatus(String id) {
        return loginMapper.getStatus(id);
    }

    public int getLoginUser(String id) {
        return loginMapper.getLoginUser(id);
    }

    public int insertLoginInfo(String sessionId, Integer id, HttpServletRequest request) {
        Login login = new Login();
        login.setTicket(sessionId);
        login.setIsDelete(0);
        login.setUserId(id);

        Date nowDate = new Date();
        login.setLoginDate(nowDate);
        login.setExpireDate(new Date(nowDate.getTime() + 20 * 60 * 1000));

        login.setDetail(JSON.toJSONString(request));

        return loginMapper.insertLoginInfo(login);
    }
}
