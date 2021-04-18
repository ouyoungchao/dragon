package com.shiliu.dragon.controller;

import com.shiliu.dragon.security.validate.code.AuthResponse;
import com.shiliu.dragon.untils.AuthUtils;
import com.shiliu.dragon.untils.cache.SessionCache;
import com.shiliu.dragon.untils.utils.JsonUtil;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@Transactional
@RestController
@RequestMapping("/dragon/authentication")
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/logout")
    public String register(HttpServletRequest request) {
        logger.info("Begin to logout ");
        String userId = AuthUtils.getUserIdFromRequest(request);
        SessionCache.removeFromCache(userId);
        logger.info("Success logout ");
        return JsonUtil.toJson(AuthResponse.LOGOUT_SUCCESS);
    }
}
