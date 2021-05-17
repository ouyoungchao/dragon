package com.shiliu.dragon.dao.school;

import com.shiliu.dragon.model.school.School;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Repository
public class SchoolDao {

    private static Logger logger = LoggerFactory.getLogger(SchoolDao.class);

    private static String QUERY_SCHOOL_BYID = "select * from school_info where name = ?";
    private static String QUERY_SCHOOL_PAGE = "select * from school_info limit ?,?";
    private static String ADD_SCHOOL = "insert into school_info(id,name, description, url, annex) values(?,?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addSchool(School school) {
        logger.info("Begin add school {}" + school.getName());
        jdbcTemplate.update(ADD_SCHOOL,school.getId(),school.getName(),school.getDescription(),school.getUrl(), JsonUtil.toJson(school.getAnnex()));
        logger.info("Add school {} success",school.getName());
    }

    public School querySchoolByName(String name) {
        try {
            logger.info("Begin query school {}", name);
            School school = jdbcTemplate.queryForObject(QUERY_SCHOOL_BYID, new SchoolRowMapper(), name);
            logger.info("Query school {} success", name);
            return school;
        } catch (EmptyResultDataAccessException e) {
            logger.error("Query school by name EmptyResultDataAccessException ");
            return null;
        }
    }

    public List<School> querySchools(int offset, int limit){
        logger.info("Begin querySchools offset {} limit {}",offset,limit);
        try{
            List<School> schools = jdbcTemplate.query(QUERY_SCHOOL_PAGE,new SchoolRowMapper(),offset,limit);
            logger.info("Query schools success and size = {}",schools.size());
            return schools;
        }catch (EmptyResultDataAccessException e){
            logger.error("Query schools with EmptyResultDataAccessException ",e);
            return null;
        }
    }
}
