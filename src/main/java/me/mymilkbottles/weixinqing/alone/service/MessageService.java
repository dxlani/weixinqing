package me.mymilkbottles.weixinqing.alone.service;

import me.mymilkbottles.weixinqing.alone.dao.MessageDAO;
import me.mymilkbottles.weixinqing.alone.model.HostHolder;
import me.mymilkbottles.weixinqing.alone.model.Message;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.model.Weibo;
import me.mymilkbottles.weixinqing.alone.util.ContentFilter;
import me.mymilkbottles.weixinqing.alone.util.MessageType;
import me.mymilkbottles.weixinqing.alone.util.WeixinqingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/07/24 10:39.
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    UserService userService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    HostHolder hostHolder;


    public int insertUpvoteMessage(int userId, int weiboId, Date time) {
        User user = userService.getUserById(userId);

        Message message = new Message();
        /*
        hi,你好啊，有一位神秘的朋友（<a href="/user/home/1">username</a>）点赞了你的微心情，
        快去看看把!
         */
        StringBuilder sb = new StringBuilder();
        sb.append("hi,你好啊，有一位神秘的朋友");
        sb.append("（<a href=");
        sb.append("\"/user/home/");
        sb.append(userId);
        sb.append("\"");
        sb.append(">");
        sb.append(user.getUsername());
        sb.append("</a>）");
        sb.append("点赞了你的微心情，快去看看把！");

        message.setContent(sb.toString());
        message.setStime(time);
        message.setIsDelete(0);
        message.setIsRead(0);
        message.setMsgType(MessageType.ADVICE.getValue());

        message.setProducer(WeixinqingUtil.ADMIN_ID);

        Weibo weibo = weiboService.getWeiboById(weiboId);

        message.setAdvicer(weibo.getMasterId());

        return insertMessage(message);
    }


    public int insertTransmitCommentMessage(int userId, int weiboId, Date time) {
        User user = userService.getUserById(userId);

        Message message = new Message();
        /*
        hi,你好啊，有一位神秘的朋友（<a href="/user/home/1">username</a>）转发了你的微心情，
        快去看看把!
         */
        StringBuilder sb = new StringBuilder();
        sb.append("hi,你好啊，有一位神秘的朋友");
        sb.append("（<a href=");
        sb.append("\"/user/home/");
        sb.append(userId);
        sb.append("\"");
        sb.append(">");
        sb.append(user.getUsername());
        sb.append("</a>）");
        sb.append("转发了你的微心情，快去看看把！");

        message.setContent(sb.toString());
        message.setStime(time);
        message.setIsDelete(0);
        message.setIsRead(0);
        message.setMsgType(MessageType.ADVICE.getValue());

        message.setProducer(WeixinqingUtil.ADMIN_ID);

        Weibo weibo = weiboService.getWeiboById(weiboId);

        message.setAdvicer(weibo.getMasterId());

        return insertMessage(message);
    }


    public int insertMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public int getUserUnreadMessageCount(int id) {
        return messageDAO.getUserUnreadMessageCount(id);
    }

    public int getUserMessageCount(int id) {
        return messageDAO.getUserMessageCount(id);
    }

    public int getUserUnreadTypeMessageCount(int id, int type) {
        return messageDAO.getUserUnreadTypeMessageCount(id, type);
    }

    public int getUserTypeMessageCount(int id, int type) {
        return messageDAO.getUserTypeMessageCount(id, type);
    }

    public List<Message> getUserUnreadMessage(int id, int start, int end) {
        return messageDAO.getUserUnreadMessage(id, start, end);
    }

    public List<Message> getUserMessage(int id, int start, int end) {
        return messageDAO.getUserMessage(id, start, end);
    }

    public List<Message> getUserUnreadTypeMessage(int id, int type, int start, int end) {
        return messageDAO.getUserUnreadTypeMessage(id, type, start, end);
    }

    public List<Message> getUserTypeMessage(int id, int type, int start, int end) {
        return messageDAO.getUserTypeMessage(id, type, start, end);
    }

    public int insertMessage(int uid, String msgs, Date date) {
        Message message = new Message();
        message.setProducer(hostHolder.getUser().getId());
        message.setContent(ContentFilter.filter(msgs));
        message.setMsgType(MessageType.PRIVATE_MESSAGE.getValue());
        message.setIsRead(0);
        message.setIsDelete(0);
        message.setStime(date);
        message.setAdvicer(uid);
        int result = messageDAO.insertMessage(message);
        return result >= 1 ? 200 : 0;
    }

}
