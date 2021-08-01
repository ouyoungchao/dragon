package com.shiliu.dragon.dao.messages;

import com.shiliu.dragon.model.messages.MessageTypes;
import com.shiliu.dragon.model.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class MessagesMapper implements RowMapper<Messages> {
    private static Logger logger = LoggerFactory.getLogger(MessagesMapper.class);

    @Override
    public Messages mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet2Messages(resultSet);
    }

    private Messages resultSet2Messages(ResultSet resultSet) {
        Messages messages = new Messages();
        try {
            messages.setId(resultSet.getString("id"));
            messages.setUserId(resultSet.getString("userId"));
            messages.setRelatedUserId(resultSet.getString("relatedUserId"));
            messages.setRelatedUserName(resultSet.getString("relatedUserName"));
            messages.setRelatedUserPortrait(resultSet.getString("relatedUserPortrait"));
            messages.setContentId(resultSet.getString("contentId"));
            messages.setContent(resultSet.getString("content"));
            messages.setProductedTime(resultSet.getLong("productTime"));
            messages.setMessageType(MessageTypes.valueOf(resultSet.getString("messageType")));
        } catch (SQLException throwables) {
            logger.error("ResultSet2Messages error ",throwables);
        }
        return messages;
    }


}
