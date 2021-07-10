package com.shiliu.dragon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ouyangchao
 * @createTime
 * @description 粉丝控制中心
 */
@Transactional
@RestController
@RequestMapping("/dragon/fans")
public class FansController {
    private static final Logger logger = LoggerFactory.getLogger(FansController.class);
    private static final String USER_ID = "userId";

    @PostMapping("follow")
    public String follower(HttpServletRequest request) {
        logger.info("Begin follow");
        String userId = request.getParameter(USER_ID);

        return null;
    }
}
