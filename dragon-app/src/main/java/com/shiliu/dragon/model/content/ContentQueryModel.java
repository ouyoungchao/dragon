package com.shiliu.dragon.model.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ouyangchao
 * @createTime
 * @description 评论查询模型
 */
public class ContentQueryModel {
    private static final Logger logger = LoggerFactory.getLogger(ContentQueryModel.class);

    private int offSet = 0;
    private int pageSize = 10;
    private boolean orderByStarts;
    private boolean orderByComments;
    private int timeRange;

    public ContentQueryModel(HttpServletRequest request) {
        this.offSet = request.getParameter("offset") == null ? this.offSet : Integer.parseInt(request.getParameter("offset"));
        this.pageSize = request.getParameter("pageSize") == null ? this.pageSize : Integer.parseInt(request.getParameter("pageSize"));
        if(offSet < 0 || pageSize < 0 || pageSize >200){
            throw new IllegalArgumentException("Offset or pageSize error");
        }
        this.orderByStarts =Boolean.valueOf(request.getParameter("orderByStarts"));
        this.orderByComments = Boolean.valueOf(request.getParameter("orderByComments"));
        String range = request.getParameter("timeRange");
        if(range != null && !range.trim().isEmpty()){
            try {
                timeRange = Integer.parseInt(range);
            }catch (NumberFormatException numberFormatException){
                logger.warn("TimeRange param error {} ",range);
            }
        }
    }

    public String getConditionSQL() {
        String condition = "";
        if(timeRange != 0 && timeRange > 0){
            long boundary = System.currentTimeMillis() - timeRange*60*60*1000;
            condition += " where publish_time > " + boundary;
        }
        condition += " order by publish_time desc ";
        if(orderByComments){
            condition += ", comments desc ";
        }
        if(orderByStarts){
            condition += ", stars desc ";
        }
        condition += "limit " + offSet + " , " + pageSize;
        return condition;
    }
}
