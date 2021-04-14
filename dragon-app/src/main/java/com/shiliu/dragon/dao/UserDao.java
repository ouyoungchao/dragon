package com.shiliu.dragon.dao;

import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.model.user.UserExtends;
import com.shiliu.dragon.model.user.UserModifyModel;
import com.shiliu.dragon.model.user.UserQueryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@Repository
public class UserDao {
    Logger logger = LoggerFactory.getLogger(getClass());

    private static String ADD_USER_SQL = "insert into user_basic_info(id,mobile, password, origin, username,school,birthday,majorIn,sex) values(?,?,?,?,?,?,?,?,?)";
    private static String QUERY_USER_BYID = "select * from user_basic_info where id = ?";
    private static String QUERY_USER_BYMOBILE = "select * from user_basic_info where mobile = ?";
    private static String QUERY_USER_PAGE = "select * from user_basic_info limit ?,?";
    private static String QUERY_USER_CONDITION = "select * from user_basic_info ";
    private static String UPDATE_USER = "update user_basic_info set ";
    //插入头像url
    private static String ADD_PORTRAIT = "insert into user_extend_info(id,name,value) values(?,?,?)";
    //查询头像信息
    private static String QUERY_PORTRAIT = "select * from user_extend_info where id = ? and name = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addUser(User user) {
        logger.info("Begin add user {}" + user.getMobile());
        jdbcTemplate.update(ADD_USER_SQL,user.getId(), user.getMobile(), user.getPassword(), user.getOrigin(), user.getUserName(), user.getSchool(),user.getBirthday(),user.getMajorIn(),user.getSex());
        logger.info("Add user {} success",user.getMobile());
    }

    public User queryUserByMobile(String mobile){
        try{
            logger.info("Begin query user {}",mobile);
            User user = jdbcTemplate.queryForObject(QUERY_USER_BYMOBILE, new UserRowMapper(),mobile);
            logger.info("Query user {} success",user.getMobile());
            return user;
        }catch (EmptyResultDataAccessException e){
            logger.error("Query user by mobile EmptyResultDataAccessException ");
            return null;
        }
    }

    public User queryUserById(String id) {
        try{
            logger.info("Begin query user {}",id);
            User user = jdbcTemplate.queryForObject(QUERY_USER_BYID, new UserRowMapper(),id);
            logger.info("Query user {} success",id);
            return user;
        }catch (EmptyResultDataAccessException e){
            logger.error("Query user by id EmptyResultDataAccessException ");
            return null;
        }
    }

    public List<User> queryUsers(int offset, int limit){
        logger.info("Begin queryUsers offset {} limit {}",offset,limit);
        try{
            List<User> users = jdbcTemplate.query(QUERY_USER_PAGE,new UserRowMapper(),offset,limit);
            logger.info("Query users success and size = {}",users.size());
            return users;
        }catch (EmptyResultDataAccessException e){
            logger.error("Query users with EmptyResultDataAccessException ",e);
            return null;
        }
    }

    public List<User> conditionQuery(UserQueryModel userQueryModel){
        logger.info("Begin query and condition is " + userQueryModel.toString());
        try{
            String conditionSql = QUERY_USER_CONDITION + userQueryModel.condition2Sql();
            List<User> users = jdbcTemplate.query(conditionSql,new UserRowMapper());
            logger.info("Condtion query users success and size = {}",users.size());
            return users;
        }catch (EmptyResultDataAccessException e){
            logger.error("Condition query users with EmptyResultDataAccessException ",e);
            return null;
        }
    }

    /**
     * 修改用户信息接口
     * @param userModifyModel
     */
    public void updateUser(UserModifyModel userModifyModel){
        logger.info("Begin modify user {}",userModifyModel.toString());
        String updateSQL =UPDATE_USER + userModifyModel.model2Sql();
        jdbcTemplate.update(updateSQL);
        logger.info("Update user success");
    }

    /**
     * 设置头像信息
     * @param id
     * @param name
     * @param value
     */
    public void addUserPortrait(String id,String name,String value){
        logger.info("Begin to add user portrait");
        jdbcTemplate.update(ADD_PORTRAIT,id,name,value);
        logger.info("Add portrait success");
    }

    /**
     * 查询头像信息
     * @param id
     * @param name
     */
    public UserExtends queryUserPortrait(String id, String name){
        logger.info("Begin query user portrait");
        try {
            UserExtends userExtends = jdbcTemplate.queryForObject(QUERY_PORTRAIT, new UserExtendRowMapper(), id, name);
            logger.info("Query portrait success");
            return userExtends;
        }catch (EmptyResultDataAccessException e){
            logger.error("Condition query usersPortrait with EmptyResultDataAccessException ",e);
            return null;
        }
    }

}


