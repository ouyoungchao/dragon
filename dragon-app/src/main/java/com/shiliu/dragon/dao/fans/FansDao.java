package com.shiliu.dragon.dao.fans;

import com.shiliu.dragon.model.fans.Fans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Repository
public class FansDao {
    private static final Logger logger = LoggerFactory.getLogger(FansDao.class);

    private static final String ADD_FANS = "insert into user_watcher(id,follow,uper,watchTimer) values(?,?,?,?)";
    private static final String DELETE_FANS = "delete from user_watcher where uper = ? and follow = ?";
    //查询关注的人
    private static final String SELECT_UPER = "select follow,uper,id,watchTimer from user_watcher where follow = ?";
    //查询粉丝
    private static final String SELECT_FOLLOW = "select follow,uper,id,watchTimer from user_watcher where uper = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addFans(Fans fans) {
        logger.info("Begin add fans {}" + fans.toString());
        try {
            jdbcTemplate.update(ADD_FANS, fans.getId(), fans.getFollow(), fans.getUper(), fans.getWatchTimer());
            logger.info("Add fans success");
        } catch (DataAccessException e) {
            logger.error("Add fans failed ", e);
            throw e;
        }

    }

    /**
     * 取消关注
     *
     * @param fans
     */
    public void deleteFans(Fans fans) {
        logger.info("Begin cancel follow fans {}",fans.toString());
        try {
            jdbcTemplate.update(DELETE_FANS, fans.getUper(), fans.getFollow());
            logger.info("Delete fans success");
        } catch (DataAccessException e) {
            logger.error("Delete fans failed ", e);
        }
    }

    /**
     * 查询自己关注的用户
     * @param follow
     * @return
     */
    public List<Fans> queryUper(String follow){
        logger.info("Begin to get uper {}",follow);
        try {
            List<Fans> upers = jdbcTemplate.query(SELECT_UPER, new FansRowMapper(), follow);
            return upers;
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No Uper",e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 查询关注自己的用户
     * @param uper
     * @return
     */
    public List<Fans> queryFans(String uper){
        logger.info("Begin to get fans {}",uper);
        try {
            List<Fans> upers = jdbcTemplate.query(SELECT_FOLLOW, new FansRowMapper(), uper);
            return upers;
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No fans",e);
            return Collections.EMPTY_LIST;
        }
    }
}
