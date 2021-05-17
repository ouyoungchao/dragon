package com.shiliu.dragon.logs;

import com.shiliu.dragon.dao.log.LoggerDao;
import com.shiliu.dragon.model.log.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author ouyangchao
 * @createTime
 * @description 记录用户操作日志
 */
@Component
public class SecurityLogger {
    private static final Logger logger = LoggerFactory.getLogger(SecurityLogger.class);

    @Autowired
    private LoggerDao loggerDao;

    public void addOperationLogger(String userId, String uri, String remoteAddress, int status, long startTime, long endTime){
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Operations operations = new Operations(id,userId,uri,remoteAddress,status,startTime,endTime);
        loggerDao.addOperationLogger(operations);
    }

}
