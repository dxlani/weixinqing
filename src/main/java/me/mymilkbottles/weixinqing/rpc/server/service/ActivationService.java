package me.mymilkbottles.weixinqing.rpc.server.service;

import me.mymilkbottles.weixinqing.alone.dao.ActivationDAO;
import me.mymilkbottles.weixinqing.alone.model.Activation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/06/29 14:32.
 */
@Service
public class ActivationService {

    @Autowired
    ActivationDAO activationMapper;

    public String getSendKey(String activationKey) {
        return activationMapper.getSendKey(activationKey);
    }

    public int insertActivationInfo(String key, String mail, int userId, String sendKey) {
        Activation activation = new Activation();
        Date expireTime = new Date(new Date().getTime() + 24L * 60 * 60 * 1000);
        activation.setExpireTime(expireTime);
        activation.setActivationKey(key);
        activation.setMail(mail);
        activation.setUserId(userId);
        activation.setActive(0);
        activation.setSendKey(sendKey);
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

    public Activation getActivationByActivationKey(String key) {
        return activationMapper.getActivationByActivationKey(key);
    }

    public Activation getActivationBySendKey(String key) {
        return activationMapper.getActivationBySendKey(key);
    }

}
