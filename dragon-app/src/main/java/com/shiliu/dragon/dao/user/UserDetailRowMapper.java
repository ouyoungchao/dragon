package com.shiliu.dragon.dao.user;

import com.shiliu.dragon.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class UserDetailRowMapper extends UserRowMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailRowMapper.class);

    @Override
    public User sql2Model(ResultSet resultSet){
        User user = super.sql2Model(resultSet);
        if(user == null){
            return user;
        }
        try {
            user.setPassword(resultSet.getString("password"));
        } catch (SQLException throwables) {
            logger.warn("Rows to user error ",throwables);
        }
        return user;
    }

}
