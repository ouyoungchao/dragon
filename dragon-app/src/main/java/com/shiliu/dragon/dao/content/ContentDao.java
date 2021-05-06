package com.shiliu.dragon.dao.content;

import com.shiliu.dragon.model.content.Comments;
import com.shiliu.dragon.model.content.Content;
import com.shiliu.dragon.model.content.ContentQueryModel;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    private static final String ADD_CONTENT = "insert into content_basic_info(id,userId,message,annex,publish_time,refrence,permissions,refUser) values(?,?,?,?,?,?,?,?)";
    private static final String QUERY_CONTENT_BYID = "select * from content_basic_info where id = ?";
    //添加评论
    private static final String ADD_COMMENTS = "insert into content_comments(id,contentId,userId,message,comments_time,starts) values(?,?,?,?,?,?)";
    //查询帖子的所有评论
    private static final String QUERY_COMMENTS_BYID = "select * from content_comments where contentId = ?";
    //查询帖子
    private static final String QUERY_CONTENTS = "select * from content_basic_info ";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addContent(Content content){
        logger.info("{} begin add content",content.getUserId());
        jdbcTemplate.update(ADD_CONTENT,content.getId(),content.getUserId(),content.getMessage(), JsonUtil.toJson(content.getAnnex()),content.getPublishTime(),content.getRefrence(),content.getPermissions(),JsonUtil.toJson(content.getRefUsers()));
        logger.info("Add content success");
    }

    public Content queryContentById(String id){
        try {
            logger.info("Begin query content {}", id);
            Content content = jdbcTemplate.queryForObject(QUERY_CONTENT_BYID, new ContentRowMapper(), id);
            logger.info("Success query content {}", id);
            return content;
        }catch (EmptyResultDataAccessException e){
            logger.error("Query content by id EmptyResultDataAccessException ");
            return null;
        }
    }

    /**
     * 添加评论
     * @param comments
     */
    public void addComments(Comments comments) {
        logger.info("{} begin add content", comments.getUserId());
        jdbcTemplate.update(ADD_COMMENTS,comments.getId(),comments.getContentId(),comments.getUserId(),comments.getMessage(),comments.getCommmentTime(),comments.getStarts());
        logger.info("Add comments success");
    }

    /**
     * 查询所有评论
     * @param contentId
     */
    public List<Comments> queryContentAllComments(String contentId) {
        logger.info("Begin query {} all commments}", contentId);
        try {
            List<Comments> comments = jdbcTemplate.query(QUERY_COMMENTS_BYID,new CommentRowMapping(),contentId);
            logger.info("Query all comments success and size = {}",comments.size());
            return comments;
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            logger.warn("No comments");
            return Collections.EMPTY_LIST;
        }

    }

    public List<Content> queryContentByCondition(ContentQueryModel contentQueryModel) {
        logger.info("Begin conditionQuery {} ",contentQueryModel);
        String sql = QUERY_CONTENTS + contentQueryModel.getConditionSQL();
        try {
            List<Content> contents = jdbcTemplate.query(sql, new ContentRowMapper());
            logger.info("Query contents success and size = {}",contents.size());
            return contents;
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            logger.warn("No condition contents");
            return Collections.EMPTY_LIST;
        }
    }
}
