package me.mymilkbottles.weixinqing.aop.logAnalysis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class IndexVisitAop {

    private static final Logger log = LoggerFactory.getLogger(IndexVisitAop.class);

    /**
     * 环绕通知
     * @param joinPoint 可用于执行切点的类
     * @return
     * @throws Throwable
     */
    @Around("execution(* me.mymilkbottles.weixinqing.controller.IndexController.index(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = (Object) joinPoint.proceed();
        } catch (Throwable th) {
        }

        long costTime = System.currentTimeMillis() - start;

        HttpServletRequest request = (HttpServletRequest) (joinPoint.getArgs()[0]);

        String acceptLanguage = request.getHeader("accept-language");
        String userAgent = request.getHeader("user-agent");

        String userIp = getRemortIP(request);
        String sessionId = request.getSession().getId();

        String ticket = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("weixinqing_ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("acceptLanguage=" + acceptLanguage);
        sb.append("&userAgent=" + userAgent);
        sb.append("&userIp=" + userIp);
        sb.append("&sessionId=" + sessionId);
        sb.append("&ticket=" + ticket);
        sb.append("&costTime=" + costTime);

        log.info(AopLogUtil.getIndexVisitAopLog(sb.toString()));
        return obj;
    }

    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
