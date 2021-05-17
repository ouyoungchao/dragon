package com.shiliu.dragon.dao.log;

import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.log.Operations;
import com.shiliu.dragon.model.user.HotUserModule;
import com.shiliu.dragon.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Component
public class LoggerDao {
    private static final Logger logger = LoggerFactory.getLogger(LoggerDao.class);

    /**
     * 添加操作日志
     */
    private static String ADD_OPERATION = "insert into operation_info(id,userId,address,uri,status,start_time,end_time) values(?,?,?,?,?,?,?)";

    /**
     * 查询热门用户Id
     */
    private static String QUERY_HOT_USERS = "select distinct userid from operation_info where start_time > ? limit ?,?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    public void addOperationLogger(Operations operations) {
        try {
            jdbcTemplate.update(ADD_OPERATION, operations.getId(), operations.getUserId(), operations.getRemoteAddress(), operations.getUri(), operations.getStatus(), operations.getStartTime(), operations.getEndTime());
        } catch (Exception exception) {
            logger.warn("Add operation error ", exception);
        }
    }

    public List<User> queryHotUser(HotUserModule userQueryModel) {
        try {
            List<Operations> operationsList = jdbcTemplate.query(QUERY_HOT_USERS,new LoggerRowMapper(),userQueryModel.getBeginTime(),userQueryModel.getOffset(),userQueryModel.getPageSize());
            List<User> users = new ArrayList<>();
            if(operationsList != null && !operationsList.isEmpty()){
                for(Operations operations : operationsList) {
                    if(operations != null) {
                        User user = userDao.queryUserById(operations.getUserId());
                        if (user != null) {
                            user.setExtendProperties(userDao.queryUserExtends(user.getId()));
                            users.add(user);
                        }
                    }
                }

            }
            return users;
        }catch (Exception exception){
            logger.warn("Query hotUser errot",exception);
        }
        return Collections.EMPTY_LIST;
    }
}
