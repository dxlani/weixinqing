package me.mymilkbottles.weixinqing.service;

/**
 * Created by Administrator on 2017/06/22 10:15.
 */

import com.alibaba.fastjson.JSON;
import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.async.HandlerCustomer;
import me.mymilkbottles.weixinqing.dao.LoginMapper;
import me.mymilkbottles.weixinqing.model.EventType;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.Login;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private static final Logger log = Logger.getLogger(LoginService.class);

    @Autowired
    LoginMapper loginMapper;

    @Autowired
    AsyncEventService asyncEventService;

    @Autowired
    HostHolder hostHolder;



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

    public int insertLoginInfo(String ticket, Integer id, HttpServletRequest request) {
        Login login = new Login();
        login.setTicket(ticket);
        login.setIsDelete(0);
        login.setUserId(id);

        Date nowDate = new Date();
        login.setLoginDate(nowDate);
        login.setExpireDate(new Date(nowDate.getTime() + 20 * 60 * 1000));

        Map<String, Object> map = new HashMap<>();

        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            map.put(element, request.getHeader(element));
        }

        login.setDetail(JSON.toJSONString(map));

        Map<String, Object> exts = new HashMap<String, Object>();

        exts.put("username", hostHolder.getUser().getUsername());

        asyncEventService.sendEvent(new EventModel(1, EventType.LOGIN, 1, 1, exts));

        return loginMapper.insertLoginInfo(login);
    }
}
