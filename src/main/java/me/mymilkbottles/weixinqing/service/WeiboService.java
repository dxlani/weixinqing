package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.async.EventModel;
import me.mymilkbottles.weixinqing.async.HandlerProducer;
import me.mymilkbottles.weixinqing.dao.JedisDAO;
import me.mymilkbottles.weixinqing.dao.WeiboDAO;
import me.mymilkbottles.weixinqing.model.*;
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

    @Autowired
    HostHolder hostHolder;

    @Autowired
    HandlerProducer handlerProducer;

    @Autowired
    CommentsService commentsService;

    public int insertWeibo(Weibo weibo) {

        if (weiboMapper.insertWeibo(weibo) > 0) {
            EventModel eventModel = new EventModel();

            eventModel.setEventId(hostHolder.getUser().getId()).setEventType(EventType.FIRE_WEIBO)
                    .addExt("time", new Date()).setEventId(weibo.getId());

            handlerProducer.produceHandler(eventModel);

            return 1;
        }

        return 0;
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

        List<ViewObject> viewObjectList = new ArrayList<>(pageCount);

        ViewObject vo = new ViewObject();

        User loginUser = hostHolder.getUser();
        int loginUserId = loginUser.getId();
        int weiboValue = EntityType.WEIBO.getValue();
        for (Weibo weibo : weiboList) {
            ViewObject viewObject = new ViewObject();
            viewObject.add("weibo", weibo);

            int userId = weibo.getId();
            User user = userService.getUserById(userId);
            viewObject.add("user", user);

            int weiboId = weibo.getId();
            viewObject.add("comms", commentsService.getWeiboCommentCount(weiboId));
            viewObject.add("ups", jedisAdapter.getUpvoteCount(loginUserId, weiboValue, weiboId));
            viewObject.add("upvote",
                    jedisAdapter.isUserUpvote(loginUserId, weiboValue, weiboId));
            viewObject.add("collection", jedisAdapter.isUserCollection(loginUserId, weiboValue, weiboId));

            viewObject.add("imgs", getWeiboImgs(weibo.getImg()));
            viewObjectList.add(viewObject);
        }
        return vo;
    }

    public int getUserWeiboCountAfterDate(int userId, Date date) {
        return weiboMapper.getUserWeiboCountAfterDate(userId, date);
    }

    public List<String> getWeiboImgs(String imgString) {
        List<String> weiboImgs = new ArrayList<>();
        if (imgString != null) {
            String[] weiboImgStrings = imgString.split("&");

            for (String weiboImg : weiboImgStrings) {
                if (StringUtils.isNotBlank(weiboImg)) {
                    weiboImgs.add(weiboImg);
                }
            }
        }
        return weiboImgs;
    }

    public List<Weibo> getWeibo(int start, int end) {
        return weiboMapper.getWeibo(start, end);
    }

    public int upvote(int userId, int entityType, int entityId) {
        return jedisAdapter.upvote(userId, entityType, entityId);
    }

    public int collection(int userId, int entityType, int entityId) {
        return jedisAdapter.collection(userId, entityType, entityId);
    }
}
