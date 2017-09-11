package me.mymilkbottles.weixinqing.util;

/**
 * Created by Administrator on 2017/06/24 12:01.
 */
public enum MailTemplateType {

    LOGIN_MAIL("loginMail.ftl"),
    REGISTER_MAIL("activationMail.ftl");

    private String type;

    MailTemplateType() {
    }

    MailTemplateType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
