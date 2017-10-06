package me.mymilkbottles.weixinqing.aop.logAnalysis;

public class AopLogUtil {

    public static String getTimeAopLog(String methodAllName, long timeCost) {
        return "TIME$$$" + methodAllName + "=" + timeCost;
    }

    public static String getIndexVisitAopLog(String detail) {
        return "INDEX_VISIT$$$" + detail;
    }

    public static String getUserActionAopLog(String methodName, String detail) {
        return "U_ACTION$$$operation=" + methodName + "$$detail" + detail;
    }

}
