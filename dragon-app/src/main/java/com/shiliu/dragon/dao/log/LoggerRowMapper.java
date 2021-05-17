package com.shiliu.dragon.dao.log;

import com.shiliu.dragon.model.log.Operations;
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
public class LoggerRowMapper implements RowMapper<Operations> {
    private static final Logger logger = LoggerFactory.getLogger(LoggerRowMapper.class);

    @Override
    public Operations mapRow(ResultSet resultSet, int i) throws SQLException {
        return sql2Module(resultSet);
    }

    private Operations sql2Module(ResultSet resultSet) {
        if(resultSet == null){
            return null;
        }
        Operations operations = new Operations();
        try {
            // TODO: 2021/4/23 改成使用反射方式赋值
           operations.setUserId(resultSet.getString("userid"));
            return operations;
        } catch (SQLException throwables) {
            logger.warn("Rows to operations error ",throwables);
            return null;
        }

    }
}
