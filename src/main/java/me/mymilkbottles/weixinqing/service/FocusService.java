package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.FocusDAO;
import me.mymilkbottles.weixinqing.model.Focus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/07/05 10:50.
 */
@Service
public class FocusService {

    @Autowired
    FocusDAO focusMapper;

    public int focus(int masterId, int slaveId) {
        Focus focus = new Focus();
        focus.setMasterId(masterId);
        focus.setSlaveId(slaveId);
        return focusMapper.focus(focus);
    }

    public int unFocus(int masterId, int slaveId) {
        return focusMapper.unFocus(masterId, slaveId);
    }

    public boolean isFocus(int masterId, int slaveId) {
        return focusMapper.isFocus(masterId, slaveId);
    }

    public int slaveCount(int userId) {
        return focusMapper.slaveCount(userId);
    }

    public int masterCount(int userId) {
        return focusMapper.masterCount(userId);
    }

    public List<Integer> getMasterUser(int userId) {
        return focusMapper.getMasterUser(userId);
    }

    public List<Integer> getSlaveUser(int userId) {
        return focusMapper.getSlaveUser(userId);
    }

    public List<Integer> getFriendUsers(int userId) {
        List<Integer> slaveUsers = focusMapper.getSlaveUser(userId);
        List<Integer> masterUsers = focusMapper.getMasterUser(userId);
        if (slaveUsers.size() > masterUsers.size()) {
            return getCommonUsers(slaveUsers, masterUsers);
        } else {
            return getCommonUsers(masterUsers, slaveUsers);
        }
    }

    private List<Integer> getCommonUsers(List<Integer> muchUsers, List<Integer> lessUsers) {
        Set<Integer> set = new HashSet<>(muchUsers);
        List<Integer> commonUsers = new ArrayList<>(lessUsers.size());
        for (Integer id : lessUsers) {
            if (set.contains(id)) {
                commonUsers.add(id);
            }
        }
        return commonUsers;
    }

    public boolean isFriend(int userIdA, int userIdB) {
        return isFocus(userIdA, userIdB) && isFocus(userIdB, userIdA);
    }
}
