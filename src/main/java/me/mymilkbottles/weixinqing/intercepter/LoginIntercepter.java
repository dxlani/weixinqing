package me.mymilkbottles.weixinqing.intercepter;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.service.LoginService;
import me.mymilkbottles.weixinqing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2017/06/21 23:20.
 */
@Component
public class LoginIntercepter implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String ticket = null;
        for (Cookie cookie : request.getCookies()) {
            if ("weixinqing_ticket".equals(cookie.getName())) {
                ticket = cookie.getValue();
                break;
            }
        }

        Date expireDate = loginService.getExpireDate(ticket);

        if (expireDate == null || new Date().before(expireDate) || loginService.getStatus(ticket) != 0) {
            return true;
        }

        int userId = loginService.getLoginUser(ticket);
        User user = userService.getUserById(userId);
        hostHolder.setUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
