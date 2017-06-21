package me.mymilkbottles.weixinqing.model;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/06/21 22:07.
 */
@Component
public class HostHolder {

    private ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    public User getUser() {
        return userThreadLocal.get();
    }

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

}
