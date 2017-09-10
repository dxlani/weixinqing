package me.mymilkbottles.weixinqing.alone.service;

import me.mymilkbottles.weixinqing.alone.dao.FocusDAO;
import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.model.Focus;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.util.WeixinqingUtil;
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
    FocusDAO focusDAO;

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FocusService focusService;

    @Autowired
    UserService userService;

    public int focus(int masterId, int slaveId) {
        Focus focus = new Focus();
        focus.setMasterId(masterId);
        focus.setSlaveId(slaveId);
        return focusDAO.focus(focus);
    }

    public int unFocus(int masterId, int slaveId) {
        return focusDAO.unFocus(masterId, slaveId);
    }

    public boolean isFocus(int masterId, int slaveId) {
        return focusDAO.isFocus(masterId, slaveId);
    }

    public int slaveCount(int userId) {
        return focusDAO.slaveCount(userId);
    }

    public int masterCount(int userId) {
        return focusDAO.masterCount(userId);
    }

    public List<Integer> getMasterUser(int userId) {
        return focusDAO.getMasterUser(userId);
    }

    public List<Integer> getSlaveUser(int userId) {
        return focusDAO.getSlaveUser(userId);
    }

    public List<Integer> getActiveSlaveUser(int userId) {
        return focusDAO.getActiveSlaveUser(userId, jedisDAO.getActivers());
    }

    public List<Integer> getActiveFriend(int userId) {
        List<Integer> activers = WeixinqingUtil.toIntegerList(jedisDAO.getActivers());
        List<Integer> friends = focusService.getFriendUsers(userId);
        return getCommonUsers(activers, friends);
    }

    public List<Integer> getFriendUsers(int userId) {
        List<Integer> slaveUsers = focusDAO.getSlaveUser(userId);
        List<Integer> masterUsers = focusDAO.getMasterUser(userId);
        return getCommonUsers(masterUsers, slaveUsers);
    }

    private List<Integer> getCommonUsers(List<Integer> list1, List<Integer> list2) {
        if (list1.size() < list2.size()) {
            return getCommonUsers(list2, list1);
        }
        Set<Integer> set = new HashSet<>(list1);
        List<Integer> commonUsers = new ArrayList<>(list2.size());
        for (Integer id : list2) {
            if (set.contains(id)) {
                commonUsers.add(id);
            }
        }
        return commonUsers;
    }

    public boolean isFriend(int userIdA, int userIdB) {
        return isFocus(userIdA, userIdB) && isFocus(userIdB, userIdA);
    }

    public int getSlaveUserCount(int userId) {
        return focusDAO.getSlaveUserCount(userId);
    }

    public int getMasterUserCount(int userId) {
        return focusDAO.getMasterUserCount(userId);
    }

    public List<User> getSlaveUser(Integer userId, int start, int end) {
        List<Integer> userIds = focusDAO.getSlaveUserPage(userId, start, end);
        List<User> users = new ArrayList<>(userIds.size());
        for (Integer id : userIds) {
            users.add(userService.getUserById(id));
        }
        return users;
    }

    public List<User> getMasterUser(int userId, int start, int end) {
        List<Integer> userIds = focusDAO.getMasterUserPage(userId, start, end);
        List<User> users = new ArrayList<>(userIds.size());
        for (Integer id : userIds) {
            users.add(userService.getUserById(id));
        }
        return users;
    }
}
