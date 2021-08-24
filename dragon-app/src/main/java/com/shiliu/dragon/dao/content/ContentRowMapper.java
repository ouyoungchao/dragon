package com.shiliu.dragon.dao.content;

import com.shiliu.dragon.model.common.EventsType;
import com.shiliu.dragon.model.content.Comments;
import com.shiliu.dragon.model.content.Content;
import com.shiliu.dragon.model.content.ContentEvents;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class ContentRowMapper implements RowMapper<Content> {
    private static final Logger logger = LoggerFactory.getLogger(ContentRowMapper.class);

    @Override
    public Content mapRow(ResultSet resultSet, int i) throws SQLException {
        return result2Module(resultSet);
    }

    // TODO: 2021/4/23 优化成反射模板类去获取
    private Content result2Module(ResultSet resultSet){
        if(resultSet == null){
            return null;
        }
        Content content = new Content();
        try {
            content.setId(resultSet.getString("id"));
            content.setMessage(resultSet.getString("message"));
            content.setAnnex(string2List(resultSet.getString("annex")));
            content.setUserId(resultSet.getString("userId"));
            content.setPublishTime(resultSet.getLong("publish_time"));
            content.setRefrence(resultSet.getString("refrence"));
            content.setComments(resultSet.getInt("comments"));
            content.setStarts(resultSet.getInt("starts"));
            content.setPermissions(resultSet.getString("permissions"));
            content.setSubject(resultSet.getString("subject"));
        } catch (SQLException throwables) {
            logger.warn("Set value error ",throwables);
        }
        return content;
    }

    private List<String> string2List(String value){
        if(value == null || value.trim().isEmpty()){
            return null;
        }
        List<String> result = new ArrayList();
        result = JsonUtil.readValue(value,List.class);
        return result;
    }
}

class CommentRowMapping implements RowMapper<Comments>{
    private static final Logger logger = LoggerFactory.getLogger(CommentRowMapping.class);

    @Override
    public Comments mapRow(ResultSet resultSet, int i) throws SQLException {
        if(resultSet == null){
            return null;
        }
        return row2Comments(resultSet);
    }

    private Comments row2Comments(ResultSet resultSet){
        Comments comments = new Comments();
        try {
            comments.setId(resultSet.getString("id"));
            comments.setContentId(resultSet.getString("contentId"));
            comments.setUserId(resultSet.getString("userId"));
            comments.setCommmentTime(resultSet.getLong("comments_time"));
            comments.setMessage(resultSet.getString("message"));
            comments.setStarts(resultSet.getInt("starts"));
            comments.setUserPortrait(resultSet.getString("user_portrait"));
            comments.setUserName(resultSet.getString("user_name"));
        } catch (SQLException throwables) {
            logger.warn("Row to comments error ",throwables);
        }
        return comments;
    }
}

class ContentEventsRowMapping implements RowMapper<ContentEvents>{
    private static final Logger logger = LoggerFactory.getLogger(ContentEventsRowMapping.class);

    @Override
    public ContentEvents mapRow(ResultSet resultSet, int i) throws SQLException {
        if(resultSet == null){
            return null;
        }
        return row2Event(resultSet);
    }

    private ContentEvents row2Event(ResultSet resultSet) {
        ContentEvents event = new ContentEvents();
        try {
            event.setId(resultSet.getString("id"));
            event.setUserId(resultSet.getString("userId"));
            event.setContentId(resultSet.getString("contentId"));
            event.setAccurrentTime(resultSet.getLong("occurrentTime"));
            event.setUserName(resultSet.getString("userName"));
            event.setUserPortrait(resultSet.getString("userPortrait"));
            event.setEventsType(EventsType.valueOf(resultSet.getString("eventType")));
        } catch (SQLException throwables) {
            logger.warn("Row to event error ",throwables);
        }
        return event;
    }
}

