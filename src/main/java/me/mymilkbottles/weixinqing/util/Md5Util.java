package me.mymilkbottles.weixinqing.util;

import sun.applet.Main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/06/21 17:32.
 */
public class Md5Util {

    /**
     * 对字符串md5加密
     */
    public static String getMD5(String str) {

        // 生成一个MD5加密计算摘要
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LogUtil.error("没有MD5算法" + e.getMessage());
        }
        // 计算md5函数
        md.update(str.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        return new BigInteger(1, md.digest()).toString(16);
    }

//    public static void main(String[] args) {
//        System.out.println(getMD5("abcsj11111111111111jsss"));
//    }

}
