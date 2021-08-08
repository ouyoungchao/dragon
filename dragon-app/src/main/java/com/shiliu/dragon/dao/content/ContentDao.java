package com.shiliu.dragon.dao.content;

import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.content.Comments;
import com.shiliu.dragon.model.content.Content;
import com.shiliu.dragon.model.content.ContentInfo;
import com.shiliu.dragon.model.content.ContentQueryModel;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static final String ADD_CONTENT = "insert into content_basic_info(id,userId,message,annex,publish_time,refrence,permissions,refUser,subject) values(?,?,?,?,?,?,?,?,?)";
    private static final String QUERY_CONTENT_BYID = "select * from content_basic_info where id = ?";
    //添加评论
    private static final String ADD_COMMENTS = "insert into content_comments(id,contentId,userId,message,comments_time,starts,user_name,user_portrait) values(?,?,?,?,?,?,?,?)";
    //查询帖子的所有评论
    private static final String QUERY_COMMENTS_BYCONTENTID = "select * from content_comments where contentId = ?";
    private static final String QUERY_COMMENTS_BYCOMMMENTID = "select * from content_comments where id = ?";

    //查询帖子
    private static final String QUERY_CONTENTS = "select * from content_basic_info ";
    //查询点赞信息
    private static final String QUERY_STARS = "select value from content_extend_info where id = ? and name = ?";
    //新增点赞
    private static final String ADD_STAR = "insert into content_extend_info(id,name,value) values(?,?,?)";

    //更新点赞
    private static final String UPDATE_STAR = "update content_extend_info set value = ? where id = ? and name = ?";

    //点赞字段名
    private static final String STARNAME = "star";
    private static final String VALUE = "value";

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
            logger.info("Success query content {}", id);
            ContentInfo contentInfo = new ContentInfo(content, user, comments);
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
                contentInfos.add(new ContentInfo(content, user, comments));
            }
            logger.info("Query contents success and size = {}", contents.size());
            return contentInfos;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            logger.warn("No condition contents");
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * 点赞接口
     *
     * @param userId    用户Id
     * @param contentId 论坛Id
     */
    public void addStar(String userId, String contentId) {
        logger.warn("{} begin to star {}", userId, contentId);
        final List<String> stars = new ArrayList<>();
        jdbcTemplate.query(QUERY_STARS, new String[]{contentId, STARNAME}, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                String currentStart = resultSet.getString(VALUE);
                if (!StringUtils.isEmpty(currentStart)) {
                    stars.addAll(JsonUtil.readValue(currentStart, List.class));
                }
            }
        });
        try {
            if (stars.isEmpty()) {
                stars.add(userId);
                jdbcTemplate.update(ADD_STAR, contentId, STARNAME, JsonUtil.toJson(stars));
            } else {
                stars.add(userId);
                jdbcTemplate.update(UPDATE_STAR, JsonUtil.toJson(stars), contentId, STARNAME);
            }
        } catch (DataAccessException e) {
            logger.error("Add star error ", e);
            throw e;
        }
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param contentId
     */
    public void cancelStar(String userId, String contentId) {
        logger.warn("{} begin to cancel star {}", userId, contentId);
        final List<String> stars = new ArrayList<>();
        jdbcTemplate.query(QUERY_STARS, new String[]{contentId, STARNAME}, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                String currentStart = resultSet.getString(VALUE);
                if (!StringUtils.isEmpty(currentStart)) {
                    stars.addAll(JsonUtil.readValue(currentStart, List.class));
                }
            }
        });
        try {
            if (stars.isEmpty()) {
                logger.warn("{} not star {}", userId, contentId);
            } else {
                stars.remove(userId);
                jdbcTemplate.update(UPDATE_STAR, JsonUtil.toJson(stars), contentId, STARNAME);
            }
        } catch (DataAccessException e) {
            logger.error("Cancel star error ", e);
            throw e;
        }
    }
}
