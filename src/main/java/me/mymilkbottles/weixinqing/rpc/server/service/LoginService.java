package me.mymilkbottles.weixinqing.rpc.server.service;

/**
 * Created by Administrator on 2017/06/22 10:15.
 */

import com.alibaba.fastjson.JSON;
import me.mymilkbottles.weixinqing.alone.async.EventModel;
import me.mymilkbottles.weixinqing.alone.dao.LoginDAO;
import me.mymilkbottles.weixinqing.alone.model.HostHolder;
import me.mymilkbottles.weixinqing.alone.model.Login;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.service.AsyncEventService;
import me.mymilkbottles.weixinqing.alone.util.EntityType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LoginService {

    private static final Logger log = Logger.getLogger(LoginService.class);

    @Autowired
    LoginDAO loginMapper;

    @Autowired
    AsyncEventService asyncEventService;

    @Autowired
    HostHolder hostHolder;

    public int getUserLoginTimesAfterDate(int userId, Date date) {
        return loginMapper.getUserLoginTimesAfterDate(userId, date);
    }

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
        login.setExpireDate(new Date(nowDate.getTime() + 30L * 60 * 1000));

        Map<String, Object> map = new HashMap<>();

        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            map.put(element, request.getHeader(element));
        }

        login.setDetail(JSON.toJSONString(map));

        Map<String, Object> exts = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        exts.put("username", user.getUsername());

        log.info("异步信息发送成功");
        asyncEventService.sendEvent(new EventModel(1, EntityType.LOGIN, 1, user.getId(), exts));

        return loginMapper.insertLoginInfo(login);
    }

    public int deleteLoginInfoById(Integer id) {
        return loginMapper.deleteLoginInfoById(id);
    }

    public void updateTicketExpireTime(String ticket, Date updateTime) {
        loginMapper.updateTicketExpireTime(ticket, updateTime);
    }
}
