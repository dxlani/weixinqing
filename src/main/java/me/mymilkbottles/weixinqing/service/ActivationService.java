package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.ActivationMapper;
import me.mymilkbottles.weixinqing.model.Activation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/06/29 14:32.
 */
@Service
public class ActivationService {

    @Autowired
    ActivationMapper activationMapper;

    public int insertActivationInfo(String key, String mail, int userId) {
        Activation activation = new Activation();
        Date expireTime = new Date(new Date().getTime() + 24L * 60 * 60 * 1000);
        activation.setExpireTime(expireTime);
        activation.setActivationKey(key);
        activation.setMail(mail);
        activation.setUserId(userId);
        activation.setActive(0);
        return activationMapper.insertActivationInfo(activation);
    }

    public int insertActivationInfo(Activation activation) {
        return activationMapper.insertActivationInfo(activation);
    }

    public int updateExpireTime(Date date, String key) {
        return activationMapper.updateExpireTime(date, key);
    }

    public Date getExpireTime(String key) {
        return activationMapper.getExpireTime(key);
    }

    public int getUserId(String key) {
        return activationMapper.getUserId(key);
    }

    public Activation getActivation(String key) {
        return activationMapper.getActivation(key);
    }
}
