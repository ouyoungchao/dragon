package com.shiliu.dragon.model.user;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ouyangchao
 * @createTime
 * @description 条件查询类
 */
public class UserQueryModel {
    private static final String AND = " and ";

    private int offSet = 0;

    private int pageSize = 10;

    private String origin;

    private String school;

    private String majorIn;

    public UserQueryModel(HttpServletRequest httpServletRequest) {
       this.origin = httpServletRequest.getParameter("origin") == null?null:httpServletRequest.getParameter("origin");
       this.school = httpServletRequest.getParameter("school") == null?null:httpServletRequest.getParameter("school");
       this.majorIn = httpServletRequest.getParameter("majorIn") == null?null:httpServletRequest.getParameter("majorIn");
       this.offSet = httpServletRequest.getParameter("offset") == null ? this.offSet : Integer.parseInt(httpServletRequest.getParameter("offset"));
       this.pageSize = httpServletRequest.getParameter("pageSize") == null ? this.pageSize : Integer.parseInt(httpServletRequest.getParameter("pageSize"));
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajorIn() {
        return majorIn;
    }

    public void setMajorIn(String majorIn) {
        this.majorIn = majorIn;
    }

    public String condition2Sql(){
        String conditionSql = "";
        if(!StringUtils.isBlank(school)){
            conditionSql += "school = \"" + school + "\"" + AND;
        }
        if(!StringUtils.isBlank(this.origin)){
            conditionSql +="origin = \"" + origin  + "\"" + AND;
        }
        if(!StringUtils.isBlank(majorIn)){
            conditionSql +="majorIn = \"" + majorIn + "\"" + AND;
        }
        //去掉最后的and
        if(conditionSql.length() > 0){
            conditionSql = " where " + conditionSql.substring(0,conditionSql.length() - AND.length() +1);
        }
        conditionSql += "limit " + offSet + " , " + pageSize+";";
        return conditionSql;
    }

    @Override
    public String toString() {
        return "UserQueryCondition{" +
                "offSet=" + offSet +
                ", pageSize=" + pageSize +
                ", origin='" + origin + '\'' +
                ", school='" + school + '\'' +
                ", majorIn='" + majorIn + '\'' +
                '}';
    }
}
