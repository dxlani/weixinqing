package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.dao.WeiboDAO;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.ViewObject;
import me.mymilkbottles.weixinqing.model.Weibo;
import me.mymilkbottles.weixinqing.util.EntityType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/07/02 20:16.
 */
@Service
public class WeiboService {

    @Autowired
    WeiboDAO weiboMapper;

    @Autowired
    JedisDAO jedisAdapter;

    @Autowired
    UserService userService;

    public int insertWeibo(Weibo weibo) {
        return weiboMapper.insertWeibo(weibo);
    }

    public List<Weibo> getIndexWeibo(int start, int end) {
        return weiboMapper.getWeibo(start, end);
    }

    public List<Weibo> getWeiboBrforeId(int start, int end, int id) {
        return weiboMapper.getWeiboBeforeId(start, end, id);
    }

    public Weibo getWeiboById(int id) {
        return weiboMapper.getWeiboById(id);
    }

    public void getPiarseCount(EntityType entityType, int entityId) {
//        jedisAdapter.
    }

    public ViewObject addWeiboDetail(List<Weibo> weiboList, int pageCount) {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        List<ViewObject> viewObjectList = new ArrayList<>(pageCount);

        ViewObject vo = new ViewObject();

        for (Weibo weibo : weiboList) {
            ViewObject viewObject = new ViewObject();
            viewObject.add("weibo", weibo);
            int userId = weibo.getId();
            User user = userService.getUserById(userId);
            viewObject.add("user", user);

            List<String> weiboImgs = new ArrayList<>();
            String[] weiboImgStrings = weibo.getImg().split("&");

            for (String weiboImg : weiboImgStrings) {
                if (StringUtils.isNotBlank(weiboImg)) {
                    weiboImgs.add(weiboImg);
                }
            }
            viewObject.add("imgs", weiboImgs);
            viewObjectList.add(viewObject);
        }
        vo.add("weibos", viewObjectList);
        return vo;
    }

    public int getUserWeiboCountAfterDate(int userId, Date date) {
        return weiboMapper.getUserWeiboCountAfterDate(userId, date);
    }

    public List<Weibo> getWeibo(int start, int end) {
        return weiboMapper.getWeibo(start, end);
    }
}
