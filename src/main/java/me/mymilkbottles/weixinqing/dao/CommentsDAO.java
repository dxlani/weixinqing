package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.Comments;
import me.mymilkbottles.weixinqing.model.Focus;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/07/05 10:50.
 */
@Mapper
public interface CommentsDAO {

    public static final String TABLE_NAME = " comments ";

    public static final String FIELDS = " f_time, content, master_id, entity_id, entity_type ";

    public static final String ALL_FIELD = "  id, f_time, content, master_id, entity_id, entity_type ";

    @Insert({"insert into" + TABLE_NAME + "(" + FIELDS + ") values(#{fTime}, #{content}, #{masterId}, #{entityId}, #{entityType})"})
    public int insertComments(Comments comments);

    @Update({"update " + TABLE_NAME + " set is_delete = 1 where id = #{id}"})
    public int deleteComments(int id);

    @Select({"select count(id) from " + TABLE_NAME + " where f_time > #{date} and master_id = #{userId} and (is_delete != 1 or is_delete is null)"})
    public int getUserCommentsCountAfterDate(@Param("userId") int userId, @Param("date") Date date);

    @Select({"select * from " + TABLE_NAME + " where id = #{id}"})
    public Comments getCommentsById(int id);

    //and entity_type = #{entityType}
    @Select({"select count(id) from " + TABLE_NAME + " where entity_id = #{entityId}  and (is_delete != 1 or is_delete is null)"})
    Integer getWeiboCommentCount(int id);
}
