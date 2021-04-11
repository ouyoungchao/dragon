package com.shiliu.dragon.dao;

import com.shiliu.dragon.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class UserRowMapper implements RowMapper<User> {
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
}
