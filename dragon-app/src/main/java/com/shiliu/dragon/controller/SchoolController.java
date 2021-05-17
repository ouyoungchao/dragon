package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.school.SchoolDao;
import com.shiliu.dragon.model.school.School;
import com.shiliu.dragon.model.school.SchoolResponse;
import com.shiliu.dragon.model.user.UserResponse;
import com.shiliu.dragon.security.validate.ValidateCodeException;
import com.shiliu.dragon.utils.SchoolUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author ouyangchao
 * @createTime
 * @description 查询学校信息
 */
@Transactional
@RestController
@RequestMapping("/dragon/school")
public class SchoolController {
    private static final Logger logger = LoggerFactory.getLogger(SchoolController.class);

    @Autowired
    private SchoolDao schoolDao;

    @PostMapping("/add")
    public String addSchool(@RequestBody String schoolContext){
        logger.info("Begin add school " + schoolContext);
        School school = JsonUtil.readValue(schoolContext, School.class);
        try {
            SchoolUtils.isValidSchool(school);
            setDefaultValue(school);
        } catch (IllegalArgumentException e) {
            logger.error("Check smsCode ServletRequestBindingException ", e);
            return e.getMessage();
        }
        if (schoolDao.querySchoolByName(school.getName()) != null) {
            logger.error("The school has register");
            return JsonUtil.toJson(SchoolResponse.SCHOOL_ADD_EXIST);
        }
        schoolDao.addSchool(school);
        logger.info("Add school {} success",school.getName());
        //注册用户
        return JsonUtil.toJson(SchoolResponse.SCHOOL_ADD_SUCCESS);
    }

    @PostMapping("query")
    public String querySchools(HttpServletRequest request){
        int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
        int limit = request.getParameter("pageSize") == null ? 10 : Integer.parseInt(request.getParameter("pageSize"));
        logger.info("Begin quesy user offset {} pageSize {}", offset, limit);
        List<School> schools = schoolDao.querySchools(offset, limit);
        SchoolResponse schoolResponse = SchoolResponse.QUERYSCHOOL_SUCCESS;
        if (schools == null || schools.isEmpty()) {
            logger.warn("Schools is empty");
            schoolResponse.setMessage(Collections.EMPTY_LIST);
        } else {
            schoolResponse.setMessage(schools);
        }
        return JsonUtil.toJson(schoolResponse);
    }

    @PostMapping("/{name}")
    public String querySchoolByName(@PathVariable(name = "name") String name){
        try {
            logger.info("begin query school " + name);
            if(name.trim().isEmpty()){
                return JsonUtil.toJson(SchoolResponse.QUERYSCHOOL_BYNAME_PARAMERROR);
            }
            School school = schoolDao.querySchoolByName(name);
            SchoolResponse schoolResponse = SchoolResponse.QUERYSCHOOL_BYNAME_SUCCESS;
            if (school != null) {
                schoolResponse.setMessage(school);
                logger.info("Query school {} success", name);
            }
            logger.warn("School {} not exit", name);
            return JsonUtil.toJson(schoolResponse);
        } catch (ValidateCodeException e) {
            logger.error("Quesy school with ValidateCodeException ", e);
            return JsonUtil.toJson(SchoolResponse.QUERYSCHOOL_FAILED);
        }

    }

    private void setDefaultValue(School school) {
        school.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
    }
}
