package com.shiliu.dragon.dao.user;

import com.shiliu.dragon.model.exception.DragonException;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.model.user.UserModifyModel;
import com.shiliu.dragon.model.user.UserQueryModel;
import com.shiliu.dragon.properties.NginxProperties;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@Repository
public class UserDao {
    Logger logger = LoggerFactory.getLogger(getClass());

    private static String ADD_USER_SQL = "insert into user_basic_info(id,mobile, password, origin, username,school,birthday,majorIn,sex,description,register_time) values(?,?,?,?,?,?,?,?,?,?,?)";
    private static String QUERY_USER_BYID = "select * from user_basic_info where id = ?";
    private static String QUERY_USER_BYMOBILE = "select * from user_basic_info where mobile = ?";
    private static String QUERY_USER_PAGE = "select * from user_basic_info limit ?,?";
    private static String QUERY_USER_CONDITION = "select * from user_basic_info ";
    private static String UPDATE_USER = "update user_basic_info set ";
    //插入头像url
    private static String ADD_EXTEND_PROPERTIES = "insert into user_extend_info values";
    //更新头像信息
    private static String UPDATE_EXTEND_PROPERTIES = "update user_extend_info set value = ? where id = ? and name = ?";

    //查询头像信息
    private static String QUERY_PORTRAIT = "select * from user_extend_info where id = ? and name = ?";
    //查询扩展属性
    private static String QUERY_USER_EXTENDS = "select * from user_extend_info where id = ?";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NginxProperties nginxProperties;

    public boolean addUser(User user) {
        logger.info("Begin add user {}" + user.getMobile());
        try {
            jdbcTemplate.update(ADD_USER_SQL, user.getId(), user.getMobile(), user.getPassword(), user.getOrigin(), user.getUserName(), user.getSchool(), user.getBirthday(), user.getMajorIn(), user.getSex(), user.getDescription(), user.getRegisterTime());
            //设置用户默认头像信息
            user.addProperty(User.PORTRAITURI_NAME, nginxProperties.getPortraitUri() + User.PORTRAITURI_DEFAULT_VALUE);
            addExtendProperties(user);
            logger.info("Add user {} success", user.getMobile());
            return true;
        } catch (DataAccessException e) {
            logger.error("Add user erro ", e);
            return false;
        }
    }

    public User queryUserByMobile(String mobile) {
        User user = null;
        try {
            logger.info("Begin query user {}", mobile);
            user = jdbcTemplate.queryForObject(QUERY_USER_BYMOBILE, new UserDetailRowMapper(), mobile);
            if(user != null) {
                user.setExtendProperties(queryUserExtends(user.getId()));
            }
            logger.info("Query user {} success", user.getMobile());
            return user;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query user by mobile EmptyResultDataAccessException ");
            return user;
        }
    }

    public User queryUserById(String id) {
        User user = null;
        try {
            logger.info("Begin query user {}", id);
            user = jdbcTemplate.queryForObject(QUERY_USER_BYID, new UserRowMapper(), id);
            if (user != null) {
                user.setExtendProperties(queryUserExtends(user.getId()));
            }
            logger.info("Query user {} success", id);
            return user;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query user by id EmptyResultDataAccessException ");
            return user;
        }
    }

    public List<User> queryUsers(int offset, int limit) {
        logger.info("Begin queryUsers offset {} limit {}", offset, limit);
        try {
            List<User> users = jdbcTemplate.query(QUERY_USER_PAGE, new UserRowMapper(), offset, limit);
            if (users != null) {
                for (User user : users) {
                    user.setExtendProperties(queryUserExtends(user.getId()));
                }
            }
            logger.info("Query users success and size = {}", users.size());
            return users;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query users with EmptyResultDataAccessException ", e);
            return null;
        }
    }

    public List<User> conditionQuery(UserQueryModel userQueryModel) {
        logger.info("Begin query and condition is " + userQueryModel.toString());
        try {
            String conditionSql = QUERY_USER_CONDITION + userQueryModel.condition2Sql();
            List<User> users = jdbcTemplate.query(conditionSql, new UserRowMapper());
            if (users != null) {
                for (User user : users) {
                    user.setExtendProperties(queryUserExtends(user.getId()));
                }
            }
            logger.info("Condtion query users success and size = {}", users.size());
            return users;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Condition query users with EmptyResultDataAccessException ", e);
            return null;
        }
    }

    /**
     * 修改用户信息接口
     *
     * @param userModifyModel
     */
    public void updateUser(UserModifyModel userModifyModel) {
        logger.info("Begin modify user {}", userModifyModel.toString());
        String updateSQL = UPDATE_USER + userModifyModel.model2Sql();
        jdbcTemplate.update(updateSQL);
        logger.info("Update user success");
    }

    /**
     * 添加扩展属性
     * @param user
     * @throws DragonException
     */
    public boolean addExtendProperties(User user) {
        logger.info("Begin to add user properties {}", user.getId());
        try {
            if (user.getExtendProperties().isEmpty()) {
                return true;
            } else {
                String addExtendSQL = ADD_EXTEND_PROPERTIES;
                Iterator<Map.Entry<String, Object>> iterator = user.getExtendProperties().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = iterator.next();
                    String key = (String) entry.getKey();
                    Object value = JsonUtil.toJson(entry.getValue());
                    if(entry.getValue() instanceof String){
                        value = (String)entry.getValue();
                    }
                    addExtendSQL += "(\'" + user.getId() + "\',\'" + key + "\',\'" + value + "\'),";
                }
                addExtendSQL = addExtendSQL.substring(0, addExtendSQL.length() - 1);
                logger.info("add extend info "+addExtendSQL);
                jdbcTemplate.update(addExtendSQL);
            }
            logger.info("Add {} success", user.getUserName());
            return true;
        } catch (DataAccessException accessException) {
            logger.error("Update user extend properties error",accessException);
            return false;
        }
    }

    public void updateExtendProperties(String id, String name, String value) throws DragonException {
        logger.info("Begin to update user {}", name);
        try {
            jdbcTemplate.update(UPDATE_EXTEND_PROPERTIES, value, id, name);
            logger.info("Update {} success", name);
        } catch (DataAccessException e) {
            logger.error("Update extend properties error", e);
            throw new DragonException("Update user extend properties error");
        }
    }

    /**
     * 查询头像信息
     *
     * @param id
     * @param name
     */
    public Map queryUserPortrait(String id, String name) {
        logger.info("Begin query user portrait");
        try {
            Map map = jdbcTemplate.queryForObject(QUERY_PORTRAIT, new UserExtendRowMapper(), id, name);
            logger.info("Query portrait success");
            return map;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Condition query usersPortrait with EmptyResultDataAccessException ", e);
            return null;
        }
    }

    /**
     * 查询用户扩展信息
     *
     * @param id
     * @return
     */
    public Map queryUserExtends(String id) {
        logger.info("Begin query user {} extends", id);
        Map map = new HashMap();
        try {
            List<Map> maps = jdbcTemplate.query(QUERY_USER_EXTENDS, new UserExtendRowMapper(), id);
            if (!maps.isEmpty()) {
                maps.stream().forEach(m -> {
                    map.putAll(m);
                });
            }
            logger.info("Query user extends success {}",map);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query user extends with EmptyResultDataAccessException ", e);
        }
        return map;
    }
}


