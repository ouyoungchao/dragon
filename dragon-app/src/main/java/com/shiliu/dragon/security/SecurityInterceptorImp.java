package com.shiliu.dragon.security;

import com.shiliu.dragon.logs.SecurityLogger;
import com.shiliu.dragon.security.validate.interceptor.SecurityInterceptor;
import com.shiliu.dragon.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.*;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Component
public class SecurityInterceptorImp implements SecurityInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptorImp.class);
    private static ExecutorService executors = Executors.newCachedThreadPool();
    private static ThreadLocal<LoggerHandler> threadLocal = new ThreadLocal<>();

    @Autowired
    private SecurityLogger securityLogger;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.debug("{} enter dragon system {}", httpServletRequest.getRemoteAddr(),httpServletRequest.getRequestURI());
        String userId = AuthUtils.getUserIdFromRequest(httpServletRequest);
        LoggerHandler loggerHandler = new LoggerHandler(userId, httpServletRequest.getRemoteAddr(), httpServletRequest.getRequestURI(), securityLogger);
        loggerHandler.setStartTime(System.currentTimeMillis());
        threadLocal.set(loggerHandler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        try {
            LoggerHandler loggerHandler = threadLocal.get();
            loggerHandler.setStatus(httpServletResponse.getStatus());
            loggerHandler.setEndTime(System.currentTimeMillis());
            executors.submit(loggerHandler);
        }catch (RejectedExecutionException rejectedExecutionException){
            logger.warn("Add operation logger failed ",rejectedExecutionException);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

class LoggerHandler implements Callable{

    private String userId;
    private String remoteAddress;
    private String uri;
    private SecurityLogger securityLogger;
    private int status;
    private long startTime;
    private long endTime;

    public LoggerHandler(String userId, String remoteAddress, String uri, SecurityLogger securityLogger) {
        this.userId = userId;
        this.remoteAddress = remoteAddress;
        this.uri = uri;
        this.securityLogger = securityLogger;
    }

    @Override
    public Object call() throws Exception {
        securityLogger.addOperationLogger(userId,uri,remoteAddress,status,startTime,endTime);
        return null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SecurityLogger getOperationLogger() {
        return securityLogger;
    }

    public void setOperationLogger(SecurityLogger securityLogger) {
        this.securityLogger = securityLogger;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
