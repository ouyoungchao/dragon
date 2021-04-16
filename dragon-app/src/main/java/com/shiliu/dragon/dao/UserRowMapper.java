package com.shiliu.dragon.dao;

import com.shiliu.dragon.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class UserRowMapper implements RowMapper<User> {
    private static Logger logger = LoggerFactory.getLogger(UserRowMapper.class);

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return sql2Model(resultSet);
    }

    private User sql2Model(ResultSet resultSet){
        if(resultSet == null){
            return null;
        }
        User user = new User();
        try {
            user.setId(resultSet.getString("id"));
            user.setBirthday(resultSet.getLong("birthday"));
            user.setSex(resultSet.getByte("sex"));
            user.setUserName(resultSet.getString("username"));
            user.setMobile(resultSet.getString("mobile"));
            user.setPassword(resultSet.getString("password"));
            user.setOrigin(resultSet.getString("origin"));
            user.setSchool(resultSet.getString("school"));
            user.setMajorIn(resultSet.getString("majorIn"));
            user.setDescription(resultSet.getString("description"));
        } catch (SQLException throwables) {
            logger.warn("Rows to user error ",throwables);
        }
        return user;
    }
}


class UserExtendRowMapper implements RowMapper<Map>{
    private static Logger logger = LoggerFactory.getLogger(UserExtendRowMapper.class);

    Map<String,Object> map = new HashMap();

    @Override
    public Map mapRow(ResultSet resultSet, int i) throws SQLException {
        if(resultSet == null){
            return null;
        }
        try {
            map.put(resultSet.getString("name"),resultSet.getString("value"));
        }catch (SQLException throwables) {
            logger.warn("Rows to userextend error ",throwables);
        }
        return map;
    }
}
