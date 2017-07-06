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
    CommentsDAO commentsMapper;

    public int insertComments(Comments comments) {
        return commentsMapper.insertComments(comments);
    }

    public int insertComments(int weiboId, int masterId, String content, Date fTime) {
        Comments comments = new Comments();
        comments.setWeiboId(weiboId);
        comments.setMasterId(masterId);
        comments.setContent(content);
        comments.setfTime(fTime);
        return commentsMapper.insertComments(comments);
    }

    public int deleteComments(int id) {
        return commentsMapper.deleteComments(id);
    }

    public int getUserCommentsCountAfterDate(int userId, Date date) {
        return commentsMapper.getUserCommentsCountAfterDate(userId, date);
    }

    public Comments getCommentsById(int id) {
        return commentsMapper.getCommentsById(id);
    }
}
