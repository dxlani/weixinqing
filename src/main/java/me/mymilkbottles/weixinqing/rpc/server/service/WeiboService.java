package me.mymilkbottles.weixinqing.rpc.server.service;

import me.mymilkbottles.weixinqing.alone.async.EventModel;
import me.mymilkbottles.weixinqing.alone.async.HandlerProducer;
import me.mymilkbottles.weixinqing.alone.dao.JedisDAO;
import me.mymilkbottles.weixinqing.alone.dao.WeiboDAO;
import me.mymilkbottles.weixinqing.alone.model.HostHolder;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.model.ViewObject;
import me.mymilkbottles.weixinqing.alone.model.Weibo;
import me.mymilkbottles.weixinqing.alone.util.ContentFilter;
import me.mymilkbottles.weixinqing.alone.util.EntityType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/07/02 20:16.
 */
@Service
public class WeiboService {

    @Value("${weixinqing.img.salt}")
    String imgSalt;

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

    @Autowired
    MessageService messageService;

    @Autowired
    JedisDAO jedisDAO;

    @Autowired
    FeedService feedService;

    public int insertWeibo(Weibo weibo) {
        if (weiboMapper.insertWeibo(weibo) > 0) {
            EventModel eventModel = new EventModel();
            eventModel.setProducer(hostHolder.getUser().getId()).setEntityType(EntityType.FIRE_WEIBO)
                    .addExt("time", new Date()).setEventId(weibo.getId());
            handlerProducer.produceHandler(eventModel);
            return 1;
        }
        return 0;
    }

    public List<Weibo> getIndexWeibo(int start, int end) {
        return weiboMapper.getWeibo(start, end);
    }

    public List<Weibo> getWeiboBeforeId(int id, int count) {
        return weiboMapper.getWeiboBeforeId(id, count);
    }

    public Weibo getWeiboById(int id) {
        return weiboMapper.getWeiboById(id);
    }

    public void getPiarseCount(EntityType entityType, int entityId) {
//        jedisAdapter.
    }

    public List<ViewObject> addWeiboDetail(ViewObject vo, int pageCount) {

        List<Weibo> weiboList = (List<Weibo>) vo.get("weibos");
        List<User> users = (List<User>) vo.get("users");

        List<ViewObject> viewObjectList = new ArrayList<>(pageCount);

        User loginUser = hostHolder.getUser();
        int loginUserId = loginUser == null ? -1 : loginUser.getId();
        int weiboValue = EntityType.WEIBO.getValue();

        int index = 0;
        for (Weibo weibo : weiboList) {
            ViewObject viewObject = new ViewObject();
            viewObject.add("weibo", weibo);

            weibo.setContent(weibo.getContent().replace("width:1em;height:1em;", "width:22px;height:22px;"));

            int userId = weibo.getMasterId();
            User user = userService.getUserById(userId);
            viewObject.add("user", users.get(index));

            int weiboId = weibo.getId();
            viewObject.add("comms", commentsService.getWeiboCommentCount(weiboId));
            viewObject.add("ups", jedisAdapter.getUpvoteCount(loginUserId, EntityType.UPVOTE.getValue(), weiboId));
            viewObject.add("upd",
                    jedisAdapter.isUserUpvote(loginUserId, EntityType.UPVOTE.getValue(), weiboId));
            viewObject.add("coll", jedisAdapter.isUserCollection(loginUserId, EntityType.COLLECTION.getValue(), weiboId));
            viewObject.add("trd", jedisAdapter.isUserTransmited(weiboId, loginUserId));
            viewObject.add("trs", jedisAdapter.getTransmitCount(weiboId));


            viewObject.add("imgs", getWeiboImgs(weibo.getImg()));
            viewObjectList.add(viewObject);
            ++index;
        }
        return viewObjectList;
    }


    public List<ViewObject> addWeiboDetail(List<Weibo> weiboList, int pageCount) {

        List<ViewObject> viewObjectList = new ArrayList<>(pageCount);

        User loginUser = hostHolder.getUser();
        int loginUserId = loginUser == null ? -1 : loginUser.getId();
        int weiboValue = EntityType.WEIBO.getValue();
        for (Weibo weibo : weiboList) {
            ViewObject viewObject = new ViewObject();
            viewObject.add("weibo", weibo);

            weibo.setContent(weibo.getContent().replace("width:1em;height:1em;", "width:22px;height:22px;"));
            int userId = weibo.getMasterId();
            User user = userService.getUserById(userId);
            viewObject.add("user", user);

            int weiboId = weibo.getId();
            viewObject.add("comms", commentsService.getWeiboCommentCount(weiboId));
            viewObject.add("ups", jedisAdapter.getUpvoteCount(loginUserId, EntityType.UPVOTE.getValue(), weiboId));
            viewObject.add("upd",
                    jedisAdapter.isUserUpvote(loginUserId, EntityType.UPVOTE.getValue(), weiboId));
            viewObject.add("coll", jedisAdapter.isUserCollection(loginUserId, EntityType.COLLECTION.getValue(), weiboId));
            viewObject.add("trd", jedisAdapter.isUserTransmited(weiboId, loginUserId));
            viewObject.add("trs", jedisAdapter.getTransmitCount(weiboId));

            viewObject.add("imgs", getWeiboImgs(weibo.getImg()));
            viewObjectList.add(viewObject);
        }
        return viewObjectList;
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
                    weiboImgs.add(weiboImg );
//                    weiboImgs.add(Md5Util.getMD5(weiboImg + imgSalt));
                }
            }
        }
        return weiboImgs;
    }

    public List<Weibo> getWeibo(int start, int end) {
        return weiboMapper.getWeibo(start, end);
    }

    public int upvote(int userId, EntityType entityType, int entityId) {

        if (jedisAdapter.upvote(userId, entityType.getValue(), entityId) == 0) {
            feedService.deleteFeeds(userId, entityType.getValue(), entityId);
            return 0;
        }

        EventModel eventModel = new EventModel();

        eventModel.setProducer(userId).setEntityType(entityType).setEventId(entityId)
                .addExt("time", new Date());

        handlerProducer.produceHandler(eventModel);

        return 1;
    }

    public int collection(int userId, int entityType, int entityId) {
        return jedisAdapter.collection(userId, entityType, entityId);
    }


    public int transmit(int weiboId, String comment) {
        comment = ContentFilter.filter(comment);
        int loginUserId = hostHolder.getUser().getId();
        if (jedisDAO.transmit(weiboId, loginUserId) == 0) {
            return 0;
        }

        EventModel eventModel = new EventModel();
        eventModel.setEventId(weiboId).setProducer(loginUserId)
                .addExt("time", new Date());

        if (StringUtils.isNotBlank(comment)) {
            eventModel.setEntityType(EntityType.TRANSMIT_COMMENT).addExt("comment", comment);
        } else {
            eventModel.setEntityType(EntityType.TRANSMIT);
        }

        handlerProducer.produceHandler(eventModel);

        return 1;
    }

//    public int transmit(int entityType, int entityId, String comment) {
//        comment = ContentFilter.filter(comment);
//        int loginUserId = hostHolder.getUser().getId();
//        if (jedisDAO.transmit(weiboId, loginUserId) == 0) {
//            return 0;
//        }
//
//        EventModel eventModel = new EventModel();
//        eventModel.setEventId(weiboId).setProducer(loginUserId)
//                .addExt("time", new Date());
//
//        if (StringUtils.isNotBlank(comment)) {
//            eventModel.setEntityType(EntityType.TRANSMIT_COMMENT).addExt("comment", comment);
//        } else {
//            eventModel.setEntityType(EntityType.TRANSMIT);
//        }
//
//        handlerProducer.produceHandler(eventModel);
//
//        return 1;
//    }

}
