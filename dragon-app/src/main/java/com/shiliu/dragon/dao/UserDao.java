package com.shiliu.dragon.dao;

import com.shiliu.dragon.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@Repository
public class UserDao {

    private static String ADD_USER_SQL = "insert into user_basic_info(mobile, password, origin, username,school,birthday,majorIn,sex) values(?,?,?,?,?,?,?,?)";
    private static String QUERY_USER_BYID = "select * from user_basic_info where mobile = ?";
    private static String QUERY_USER_PAGE = "select * from user_basic_info limit ?,?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addUser(User user) {
        System.out.println("begin add user" + user);
        jdbcTemplate.update(ADD_USER_SQL, user.getMobile(), user.getPassword(), user.getOrigin(), user.getUserName(), user.getSchool(),user.getBirthday(),user.getMajorIn(),user.getSex());
    }

    public User queryUserById(String id) {
        try{
            User user = jdbcTemplate.queryForObject(QUERY_USER_BYID, new UserRowMapper(),id);
            return user;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<User> queryUsers(int offset, int limit){
        return jdbcTemplate.query(QUERY_USER_PAGE,new UserRowMapper(),offset,limit);
    }

}
