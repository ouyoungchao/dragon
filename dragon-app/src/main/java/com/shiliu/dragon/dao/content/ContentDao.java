package com.shiliu.dragon.dao.content;

import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.common.EventsType;
import com.shiliu.dragon.model.content.*;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Repository
public class ContentDao {
    private static final Logger logger = LoggerFactory.getLogger(ContentDao.class);
    //发帖
    private static final String ADD_CONTENT = "insert into content_basic_info(id,userId,message,annex,publish_time,refrence,permissions,refUser,subject) values(?,?,?,?,?,?,?,?,?)";
    //查询帖子
    private static final String QUERY_CONTENT_BYID = "select * from content_basic_info where id = ?";
    //删贴
    private static final String DELETE_CONTENT_BYID = "delete from content_basic_info where id = ?";
    //删除论坛的所有评论
    private static final String DELETE_CONTENT_COMMENT = "delete from content_comments where contentId = ?";
    //删除单条评论
    private static final String DELETE_COMMENT_BYID = "delete from content_comments where id = ?";
    //删除收藏和点赞
    private static final String DETELE_STAR_AND_COLLECT = "delete from content_stars_and_collect where contentId = ?";

    private static final String ADD_COMMENTS = "insert into content_comments(id,contentId,userId,message,comments_time,starts,user_name,user_portrait) values(?,?,?,?,?,?,?,?)";
    //查询帖子的所有评论
    private static final String QUERY_COMMENTS_BYCONTENTID = "select * from content_comments where contentId = ?";
    private static final String QUERY_COMMENTS_BYCOMMMENTID = "select * from content_comments where id = ?";

    //查询帖子
    private static final String QUERY_CONTENTS = "select * from content_basic_info ";

    //查询用户的某一具体事件
    private static final String QUERY_USER_EVENT = "select * from content_stars_and_collect where contentId = ? and userId = ? and eventType = ?";

    //查询用户的全部事件
    private static final String QUERY_USER_EVENTS = "select * from content_stars_and_collect where userId = ? and eventType = ?";

    //查询帖子点赞或者收藏
    private static final String QUERY_CONTENT_EVENT = "select * from content_stars_and_collect where contentId = ? and eventType = ?";

    //新增点赞或者收藏
    private static final String ADD_EVENT = "insert into content_stars_and_collect(id,userId,contentId,occurrentTime,userName,userPortrait,eventType) values(?,?,?,?,?,?,?)";
    //取消点赞或者收藏
    private static final String CANCEL_EVENT = "delete from content_stars_and_collect where userId = ? and contentId = ? and eventType = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    public void addContent(Content content) {
        logger.info("{} begin add content", content.getUserId());
        jdbcTemplate.update(ADD_CONTENT, content.getId(), content.getUserId(), content.getMessage(), JsonUtil.toJson(content.getAnnex()), content.getPublishTime(), content.getRefrence(), content.getPermissions(), JsonUtil.toJson(content.getRefUsers()), content.getSubject());
        logger.info("Add content success");
    }

    public ContentInfo queryContentInfoById(String id) {
        try {
            logger.info("Begin query content {}", id);
            Content content = jdbcTemplate.queryForObject(QUERY_CONTENT_BYID, new ContentRowMapper(), id);
            User user = userDao.queryUserById(content.getUserId());
            List<Comments> comments = queryContentAllComments(id);
            List<ContentEvents> stars = queryContentEvent(id, EventsType.STAR);
            logger.info("Success query content {}", id);
            ContentInfo contentInfo = new ContentInfo(content, user, comments, stars);
            return contentInfo;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query content by id EmptyResultDataAccessException ");
            return null;
        }
    }

    public Content queryContent(String id) {
        try {
            logger.info("Begin query content {}", id);
            Content content = jdbcTemplate.queryForObject(QUERY_CONTENT_BYID, new ContentRowMapper(), id);
            return content;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query content by id EmptyResultDataAccessException ");
            return null;
        }
    }

    public void deleteContent(String contentId){
        try{
            logger.info("Begin to delete content {}",contentId);
            jdbcTemplate.update(DELETE_CONTENT_BYID,contentId);
            jdbcTemplate.update(DELETE_CONTENT_COMMENT,contentId);
            jdbcTemplate.update(DETELE_STAR_AND_COLLECT,contentId);
        }catch (EmptyResultDataAccessException e){
            logger.error("Delete content {} error",contentId,e);
        }
    }

    public void deleteCommentById(String commentId){
        try{
            logger.info("Begin to delete comment {}",commentId);
            jdbcTemplate.update(DELETE_COMMENT_BYID,commentId);
        }catch (EmptyResultDataAccessException e){
            logger.error("Delete comment {} error",commentId,e);
        }
    }

    /**
     * 添加评论
     *
     * @param comments
     */
    public void addComments(Comments comments) {
        logger.info("{} begin add content", comments.getUserId());
        jdbcTemplate.update(ADD_COMMENTS, comments.getId(), comments.getContentId(), comments.getUserId(), comments.getMessage(), comments.getCommmentTime(), comments.getStarts(), comments.getUserName(), comments.getUserPortrait());
        logger.info("Add comments success");
    }

    /**
     * 查询所有评论
     *
     * @param contentId
     */
    public List<Comments> queryContentAllComments(String contentId) {
        logger.info("Begin query {} all commments}", contentId);
        try {
            List<Comments> comments = jdbcTemplate.query(QUERY_COMMENTS_BYCONTENTID, new CommentRowMapping(), contentId);
            for (Comments cm : comments) {
                queryCommentsDetail(cm);
            }
            logger.info("Query all comments success and size = {}", comments.size());
            return comments;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            logger.warn("No comments");
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 递归查询评论的所有回复
     *
     * @param comments
     * @return
     */
    public Comments queryCommentsDetail(Comments comments) {
        logger.info("Begin query comment details {} ", comments);
        //查询2级评论
        List<Comments> commentsList = queryCommentAllComments(comments.getId());
        if (commentsList.isEmpty()) {
            return comments;
        } else {
            comments.setComments(commentsList);
            for (Comments cm : commentsList) {
                //查询n级评论
                queryCommentsDetail(cm);
            }
        }
        return comments;
    }

    /**
     * 查询评论的回复
     *
     * @param contentId
     * @return
     */
    private List<Comments> queryCommentAllComments(String contentId) {
        logger.info("Begin query comment {} all commments}", contentId);
        try {
            //获取1级评论
            List<Comments> comments = jdbcTemplate.query(QUERY_COMMENTS_BYCONTENTID, new CommentRowMapping(), contentId);
            logger.info("Query all comments success and size = {}", comments.size());
            return comments;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            logger.warn("No comments");
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * 查询当条评论,不包含对评论的回复
     *
     * @param commentId
     * @return
     */
    public Comments queryComment(String commentId) {
        logger.info("Begin query {}", commentId);
        try {
            Comments comments = jdbcTemplate.queryForObject(QUERY_COMMENTS_BYCOMMMENTID, new CommentRowMapping(), commentId);
            logger.info("Query all comments success", comments);
            return comments;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            logger.warn("Comment {} not exist", commentId);
            return null;
        }
    }

    public List<ContentInfo> queryContentByCondition(ContentQueryModel contentQueryModel) {
        logger.info("Begin conditionQuery {} ", contentQueryModel);
        String sql = QUERY_CONTENTS + contentQueryModel.getConditionSQL();
        try {
            List<Content> contents = jdbcTemplate.query(sql, new ContentRowMapper());
            List<ContentInfo> contentInfos = new ArrayList<>();
            for (Content content : contents) {
                User user = userDao.queryUserById(content.getUserId());
                List<Comments> comments = queryContentAllComments(content.getId());
                List<ContentEvents> stars = queryContentEvent(content.getId(), EventsType.STAR);
                contentInfos.add(new ContentInfo(content, user, comments, stars));
            }
            logger.info("Query contents success and size = {}", contents.size());
            return contentInfos;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            logger.warn("No condition contents");
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * 点赞
     *
     * @param contentEvents
     */
    public void addEvent(ContentEvents contentEvents) {
        logger.info("{} begin to event {}", contentEvents.getUserId(), contentEvents.getContentId());
        ContentEvents event = queryUserEvent(contentEvents.getContentId(), contentEvents.getUserId(), contentEvents.getEventsType());
        if (event != null) {
            return;
        } else {
            try {
                jdbcTemplate.update(ADD_EVENT, contentEvents.getId(), contentEvents.getUserId(), contentEvents.getContentId(), contentEvents.getAccurrentTime(), contentEvents.getUserName(), contentEvents.getUserPortrait(), contentEvents.getEventsType().toString());
            } catch (DataAccessException dataAccessException) {
                logger.warn("Add event error", dataAccessException);
            }
        }
    }

    public ContentEvents queryUserEvent(String contentId, String userId, EventsType eventsType) {
        ContentEvents event = null;
        try {
            event = jdbcTemplate.queryForObject(QUERY_USER_EVENT, new ContentEventsRowMapping(), contentId, userId, eventsType.toString());
        } catch (DataAccessException dataAccessException) {
            logger.info("Query event empty ", dataAccessException);
        }
        return event;
    }

    public List<ContentEvents> queryUserEventS(String userId, EventsType eventsType) {
        List<ContentEvents> events = null;
        try {
            events = jdbcTemplate.query(QUERY_USER_EVENTS, new ContentEventsRowMapping(), userId, eventsType.toString());
        } catch (DataAccessException dataAccessException) {
            logger.info("Query event empty ", dataAccessException);
        }
        return events;
    }

    public List<ContentEvents> queryContentEvent(String contentId, EventsType eventsType) {
        logger.info("Begin query centent {} {}}", contentId, eventsType.toString());
        try {
            List<ContentEvents> events = jdbcTemplate.query(QUERY_CONTENT_EVENT, new ContentEventsRowMapping(), contentId, eventsType.toString());
            return events;
        } catch (DataAccessException dataAccessException) {
            logger.warn("Quey content stars error ", dataAccessException);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param contentId
     */
    public void cancelEvent(String userId, String contentId, EventsType eventsType) {
        logger.warn("{} begin to cancel star {}", userId, contentId);
        ContentEvents star = queryUserEvent(contentId, userId, eventsType);
        if (star == null) {
            logger.warn("User {} not star the content {}", userId, contentId);
        } else {
            try {
                jdbcTemplate.update(CANCEL_EVENT, userId, contentId, eventsType.toString());
            } catch (DataAccessException dataAccessException) {
                logger.warn("Delete star error ", dataAccessException);
            }
        }
    }
}
