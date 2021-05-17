package com.shiliu.dragon.utils;

import com.shiliu.dragon.model.school.School;
import com.shiliu.dragon.model.school.SchoolResponse;
import com.shiliu.dragon.utils.utils.JsonUtil;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class SchoolUtils {

    public static boolean isValidSchool(School school){
        if(school.getName().trim().isEmpty() || school.getDescription().trim().isEmpty()){
            throw new IllegalArgumentException(JsonUtil.toJson(SchoolResponse.SCHOOL_PARAM_ERROR));
        }
        return true;
    }
}
