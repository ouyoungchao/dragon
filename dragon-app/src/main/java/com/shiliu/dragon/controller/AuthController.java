package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.audit.AuditDao;
import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.Audit.AuditResponse;
import com.shiliu.dragon.model.Audit.AuditStatus;
import com.shiliu.dragon.model.Audit.Audits;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.properties.NginxProperties;
import com.shiliu.dragon.security.validate.AuthResponse;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.PictureUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * @author ouyangchao
 * @createTime
 * @description 认证控制器
 */

@Transactional
@RestController
@RequestMapping("/dragon/authentication")
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private NginxProperties nginxProperties;

    @Autowired
    private AuditDao auditDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/logout")
    public String register(HttpServletRequest request) {
        logger.info("Begin to logout ");
        String userId = AuthUtils.getUserIdFromRequest(request);
        redisTemplate.delete(userId);
        logger.info("Success logout ");
        return JsonUtil.toJson(AuthResponse.LOGOUT_SUCCESS);
    }

    @PostMapping("/student")
    public String studentsAuth(HttpServletRequest request) {
        return uploadAudit(request, false);
    }

    @PostMapping("/manager")
    public String managerAuth(HttpServletRequest request) {
        return uploadAudit(request, true);
    }

    /**
     * 审核
     *
     * @param request
     * @return
     */
    @PostMapping("/examine")
    public String examineAuth(HttpServletRequest request) {
        logger.info("Begin auth");
        String id = request.getParameter("taskId");
        String status = request.getParameter("status");

        if (StringUtils.isEmpty(id) || status == null || AuditStatus.valueOf(status.toUpperCase()) == null) {
            logger.error("Auth id is empty {} {}",id,status);
            return JsonUtil.toJson(AuditResponse.EXAMINE_PARAM_ERROR);
        }
        Audits audits = auditDao.queryAuditInfoById(id);
        if (audits == null) {
            logger.error("Auth not exist");
            return JsonUtil.toJson(AuditResponse.EXAMINE_PARAM_ERROR);
        }
        audits.setStatus(AuditStatus.valueOf(status.toUpperCase()));
        auditDao.updateExamineStatus(audits);
        logger.info("Examine finish and status is {}", status);
        if(status.equals(AuditStatus.REJECT)){
            return JsonUtil.toJson(AuditResponse.EXAMINE_FINISH);
        }
        if(audits.getIsManager()){
            User user = new User();
            user.setId(audits.getUserId());
            // TODO: 2021/8/25 添加多个管理员时候 需要去重处理
            user.addProperty("isManager",true);
            user.addProperty("managerId",audits.getSchool());
            userDao.addExtendProperties(user);
        }
        return JsonUtil.toJson(AuditResponse.EXAMINE_FINISH);
    }

    @PostMapping("queryExamineTasks")
    public String queryAllExamineTasks(HttpServletRequest request) {
        String managerId = AuthUtils.getUserIdFromRequest(request);
        List<Audits> audits = auditDao.queryAuditInfoByManager(managerId);
        AuditResponse response = AuditResponse.QUERY_EXAMINE_TASK_SUCCESS;
        response.setMessage(audits);
        return JsonUtil.toJson(response);
    }

    private void setDefaultValue(Audits audits) {
        audits.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
    }

    private String uploadAudit(HttpServletRequest request, boolean isManager) {
        String userId = AuthUtils.getUserIdFromRequest(request);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
        String school = request.getParameter("school");
        if (files == null || files.isEmpty() || school == null) {
            logger.warn("Param is invalid {}", files);
            return JsonUtil.toJson(AuditResponse.AUDIT_PARAM_ERROR);
        }
        //上传认证材料
        List<String> meterials = PictureUtils.uploadPicture(files, nginxProperties.getAudit(), nginxProperties.getAuditUri());
        String managerId = school;
        if (isManager) {
            managerId = "admin";
        }
        Audits audits = new Audits(userId, meterials, AuditStatus.WAITING_EXAMINE, System.currentTimeMillis(), 0l, managerId, isManager, school);
        setDefaultValue(audits);
        try {
            auditDao.addAudit(audits);
            return JsonUtil.toJson(AuditResponse.UPLOADAUDITSUCCESS);
        } catch (Exception e) {
            logger.warn("Add audits failed ", e);
            return JsonUtil.toJson(AuditResponse.UPLOADAUDITFAILED);
        }
    }
}
