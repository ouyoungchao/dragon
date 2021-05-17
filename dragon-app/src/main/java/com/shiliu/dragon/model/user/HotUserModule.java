package com.shiliu.dragon.model.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class HotUserModule implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(HotUserModule.class);
    private int offset = 0;

    private int pageSize = 10;

    /**
     * 默认24小时活跃用户
     */
    private int durations = 24;

    public HotUserModule(HttpServletRequest request) {
        try {
            this.offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
            this.pageSize = request.getParameter("pageSize") == null ? 10 : Integer.parseInt(request.getParameter("pageSize"));
            this.durations = request.getParameter("durations") == null ? 10 : Integer.parseInt(request.getParameter("durations"));
        } catch (NumberFormatException numberFormatException) {
            logger.warn("Param error ", numberFormatException);
        }
    }

    public long getBeginTime(){
        return System.currentTimeMillis() - durations*60*60*1000;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }
}
