package com.shiliu.dragon.dao.school;

import com.shiliu.dragon.model.school.School;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class SchoolRowMapper implements RowMapper<School> {
    private static Logger logger = LoggerFactory.getLogger(SchoolRowMapper.class);

    @Override
    public School mapRow(ResultSet resultSet, int i) throws SQLException {
        return sql2Model(resultSet);
    }

    public School sql2Model(ResultSet resultSet){
        if(resultSet == null){
            return null;
        }
        School school = new School();
        try {
            school.setId(resultSet.getString("id"));
            school.setName(resultSet.getString("name"));
            school.setDescription(resultSet.getString("description"));
            school.setUrl(resultSet.getString("url"));
            school.setAnnex(string2List(resultSet.getString("annex")));
        } catch (SQLException throwables) {
            logger.warn("Rows to user error ",throwables);
        }
        return school;
    }

    private List<String> string2List(String value){
        if(value == null || value.trim().isEmpty()){
            return null;
        }
        List<String> result = new ArrayList();
        result = JsonUtil.readValue(value,List.class);
        return result;
    }
}
