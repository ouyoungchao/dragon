package com.shiliu.dragon.dao.fans;

import com.shiliu.dragon.model.fans.Fans;
import com.shiliu.dragon.model.user.User;
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
public class FansRowMapper implements RowMapper<Fans> {
    private static final Logger logger = LoggerFactory.getLogger(FansRowMapper.class);

    @Override
    public Fans mapRow(ResultSet resultSet, int i) throws SQLException {
        return sql2Model(resultSet);
    }

    private Fans sql2Model(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
        try {
            String id = resultSet.getString("id");
            String upper = resultSet.getString("uper");
            String follow = resultSet.getString("follow");
            long watchTimer = resultSet.getLong("watchTimer");
            return new Fans(id, upper, follow, watchTimer);
        } catch (SQLException throwables) {
            logger.warn("Rows to user error ", throwables);
            return null;
        }
    }
}
