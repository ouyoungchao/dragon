package com.shiliu.dragon.security.validate.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author ouyangchao
 * @createTime
 * @description 安全拦截器，提供记录操作日志，添加黑名单，防暴力破解等能力
 */
public interface SecurityInterceptor extends HandlerInterceptor {
}
