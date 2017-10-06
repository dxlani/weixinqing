package me.mymilkbottles.weixinqing.aop.logAnalysis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class TimeAop {


    private static final Logger log = LoggerFactory.getLogger(TimeAop.class);

//    /**
//     * 前置通知
//     */
//    @Before("execution(* me.mymilkbottles.weixinqing.service.*.*(..))")
//    public void before(JoinPoint joinPoint){
////        System.out.println("前置通知....");
//        String methodName = joinPoint.getSignature().getName();
//        AopLog.log.info("开始执行" + methodName);
//    }

//    /**
//     * 后置通知
//     * returnVal,切点方法执行后的返回值
//     */
//    @AfterReturning(value="execution(* me.mymilkbottles.weixinqing.service.*.*(..))",returning = "returnVal")
//    public void AfterReturning(Object returnVal){
////        System.out.println("后置通知...."+returnVal);
//    }


    /**
     * 环绕通知
     * @param joinPoint 可用于执行切点的类
     * @return
     * @throws Throwable
     */
    @Around("execution(* me.mymilkbottles.weixinqing.service.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String type = joinPoint.getSignature().getDeclaringType().getTypeName();
        String methodAllName = type + "." + joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = (Object) joinPoint.proceed();
        } catch (Throwable th) {
        }
        long timeCost = System.currentTimeMillis() - start;
        log.info(AopLogUtil.getTimeAopLog(methodAllName, timeCost));
        return obj;
    }

//    /**
//     * 抛出通知
//     * @param e
//     */
//    @AfterThrowing(value="execution(* me.mymilkbottles.weixinqing.service.*.*(..))",throwing = "e")
//    public void afterThrowable(Throwable e){
//
////        System.out.println("出现异常:msg="+e.getMessage());
//    }

//    /**
//     * 无论什么情况下都会执行的方法
//     */
//    @After(value="execution(* me.mymilkbottles.weixinqing.service.*.*(..))")
//    public void after(){
//    }

}
