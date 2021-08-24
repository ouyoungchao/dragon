package com.shiliu.dragon.dao.messages;

import com.shiliu.dragon.model.common.EventsType;
import com.shiliu.dragon.model.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Repository
public class MessagesDao {
    private static final Logger logger = LoggerFactory.getLogger(MessagesDao.class);

    private static final String SELECT_MESSAGES = "select * from user_messages where userId = ?";
    private static final String DELETE_USER_MESSAGES = "delete from user_messages where userId = ?";
    private static final String ADD_MESSAGES = "insert into user_messages(id,userId,relatedUserId,relatedUserName,relatedUserPortrait,messageType,content,contentId,productTime) values (?,?,?,?,?,?,?,?,?)";
    private static final String DELETED_MESSAGES = "delete from user_messages where userId = ? and relatedUserId = ? and messageType = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Messages> queryMessages(String userId) {
        List<Messages> messages = new ArrayList<>();
        try {
            messages.addAll(jdbcTemplate.query(SELECT_MESSAGES, new MessagesMapper(), userId));
        } catch (DataAccessException exception) {
            logger.error("Query message error ", exception);
        }
        return messages;
    }

    public void deleteMessage(String userId) {
        logger.info("Begin to delete {} message", userId);
        try {
            jdbcTemplate.update(DELETE_USER_MESSAGES, userId);
        } catch (DataAccessException dataAccessException) {
            logger.error("Delete user messages error ", dataAccessException);
        }
    }

    public void addMessages(Messages messages) {
        logger.info("Begin add messages {} ", messages);
        try {
            jdbcTemplate.update(ADD_MESSAGES, messages.getId(), messages.getUserId(), messages.getRelatedUserId(), messages.getRelatedUserName(), messages.getRelatedUserPortrait(), messages.getMessageType().toString(), messages.getContent(), messages.getContentId(), messages.getProductedTime());
            logger.info("Add messages success");
        } catch (DataAccessException dataAccessException) {
            logger.warn("Insert messages error ", dataAccessException);
        }
    }

    public void deleteMessages(String userId, String relatedUserId, EventsType eventsType){
        logger.info("Begin to delete {} {} {}message", userId,relatedUserId, eventsType);
        try {
            jdbcTemplate.update(DELETED_MESSAGES, userId,relatedUserId, eventsType.toString());
        } catch (DataAccessException dataAccessException) {
            logger.error("Delete messages error ", dataAccessException);
        }
    }

}
