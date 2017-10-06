package me.mymilkbottles.weixinqing.intercepter;

import me.mymilkbottles.weixinqing.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/06/21 23:18.
 */
@Component
public class AuthorityIntercepter implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (hostHolder.getUser() == null) {
            response.sendRedirect("/login?from=" + request.getRequestURI());
            return false;
        } else if (request.getRequestURI().contains("admin")) {
            if (hostHolder.getUser().getId() != 0) {
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                response.getWriter().write("您无权访问该网页！");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
