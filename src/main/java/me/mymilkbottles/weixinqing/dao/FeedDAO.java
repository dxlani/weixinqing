package me.mymilkbottles.weixinqing.dao;

import me.mymilkbottles.weixinqing.model.Feed;
import me.mymilkbottles.weixinqing.model.Weibo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/06/21 17:19.
 */
@Mapper
public interface FeedDAO {

    public static final String TABLE_NAME = " feed ";

    public static final String FIELD = " user_id, type, weibo_id, exts_id, f_time, is_delete ";

    public static final String ALL_FIELD = " id, user_id, type, weibo_id, exts_id, f_time, is_delete ";

    @Insert({"insert into " + TABLE_NAME + "(" + FIELD + ") values(#{userId}, #{type}, #{weiboId}, #{extsId}, #{fTime}, #{isDelete})"})
    public int insertFeed(Feed feed);

    @Update({"update " + TABLE_NAME + " set is_delete = 1 where id = #{id} "})
    public int deleteFeed(int id);

    @Select({"select * from " + TABLE_NAME + " where id = #{id}"})
    public Feed getFeedById(int id);

    List<Feed> getFeeds(@Param("userIds") List<Integer> userIds, @Param("start") int start, @Param("end") int end);

    void deleteFeeds(@Param("user_id") int userId, @Param("type") int type, @Param("weibo_id") int weiboId, @Param("exts_id") Integer extId);

    @Select({"select " + ALL_FIELD + " from " + TABLE_NAME + " where user_id = #{userId} and id < #{maxId} order by f_time desc limit #{size}"})
    List<Feed> getUserFeeds(@Param("userId") int userId, @Param("maxId") int maxId, @Param("size") int pageSize);
}
