package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.FocusMapper;
import me.mymilkbottles.weixinqing.model.Focus;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/05 10:50.
 */
@Service
public class FocusService {

    @Autowired
    FocusMapper focusMapper;

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

}
