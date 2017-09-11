package me.mymilkbottles.weixinqing.service;

import me.mymilkbottles.weixinqing.dao.CommentsDAO;
import me.mymilkbottles.weixinqing.model.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/05 17:19.
 */
@Service
public class CommentsService {

    @Autowired
    CommentsDAO commentsDAO;

    public int insertComments(Comments comments) {
        return commentsDAO.insertComments(comments);
    }

    public int insertComments(int entityId, int entityType, int masterId, String content, Date fTime) {
        Comments comments = new Comments();
        comments.setEntityId(entityId);
        comments.setEntityType(entityType);
        comments.setMasterId(masterId);
        comments.setContent(content);
        comments.setfTime(fTime);
        return commentsDAO.insertComments(comments);
    }

    public int deleteComments(int id) {
        return commentsDAO.deleteComments(id);
    }

    public int getUserCommentsCountAfterDate(int userId, Date date) {
        return commentsDAO.getUserCommentsCountAfterDate(userId, date);
    }

    public Comments getCommentsById(int id) {
        return commentsDAO.getCommentsById(id);
    }

//    public Integer getWeiboCommentCount(int type, int id) {
//        return commentsDAO.getWeiboCommentCount(type, id);
//    }

    public Integer getWeiboCommentCount(int id) {
        return commentsDAO.getWeiboCommentCount(id);
    }
}
