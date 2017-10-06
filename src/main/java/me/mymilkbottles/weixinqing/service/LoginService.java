package me.mymilkbottles.weixinqing.service;

/**
 * Created by Administrator on 2017/06/22 10:15.
 */
import com.alibaba.fastjson.JSON;

import me.mymilkbottles.weixinqing.dao.LoginDAO;
import me.mymilkbottles.weixinqing.model.EventModel;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.Login;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.util.DateUtil;
import me.mymilkbottles.weixinqing.util.EntityType;
import me.mymilkbottles.weixinqing.util.IpAddressUtil;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
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

        int result = loginMapper.insertLoginInfo(login);
        if (result > 0) {
            Map<String, Object> exts = new HashMap<String, Object>();

            User user = hostHolder.getUser();
            exts.put("username", user.getUsername());

           try {
               String userAgent = request.getHeader("user-agent");

               String userIp = IpAddressUtil.getIpAddr(request);

               String loginAddress = IpAddressUtil.getAddresses(userIp);

               String[] address = loginAddress.split("-");
               exts.put("loginInet", address[3]);
               exts.put("loginAddress", address[0] + address[1] + address[2]);
               exts.put("loginTime", DateUtil.getNowDate());
           } catch (Exception e ) {
           }

            asyncEventService.sendEvent(new EventModel(1, EntityType.LOGIN, WeixinqingUtil.ADMIN_ID, user.getId(), exts));

        }

        return result;
    }

    public int deleteLoginInfoById(Integer id) {
        return loginMapper.deleteLoginInfoById(id);
    }

    public void updateTicketExpireTime(String ticket, Date updateTime) {
        loginMapper.updateTicketExpireTime(ticket, updateTime);
    }

    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
