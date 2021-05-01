package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.audit.AuditDao;
import com.shiliu.dragon.model.Audit.AuditResponse;
import com.shiliu.dragon.model.Audit.Audits;
import com.shiliu.dragon.properties.NginxProperties;
import com.shiliu.dragon.security.validate.code.AuthResponse;
import com.shiliu.dragon.untils.AuthUtils;
import com.shiliu.dragon.untils.PictureUtils;
import com.shiliu.dragon.untils.cache.SessionCache;
import com.shiliu.dragon.untils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @description
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

    @PostMapping("/logout")
    public String register(HttpServletRequest request) {
        logger.info("Begin to logout ");
        String userId = AuthUtils.getUserIdFromRequest(request);
        SessionCache.removeFromCache(userId);
        logger.info("Success logout ");
        return JsonUtil.toJson(AuthResponse.LOGOUT_SUCCESS);
    }

    @PostMapping("/student")
    public String studentsAuth(HttpServletRequest request){
        return uploadAudit(request,false);
    }

    @PostMapping("/manager")
    public String managerAuth(HttpServletRequest request){
        return uploadAudit(request,true);
    }

    private void setDefaultValue(Audits audits) {
        audits.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
    }

    private String  uploadAudit(HttpServletRequest request,boolean isManager){
        String userId = AuthUtils.getUserIdFromRequest(request);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
        if (files == null || files.isEmpty()) {
            logger.warn("Param is invalid {}", files);
            return JsonUtil.toJson(AuditResponse.AUDIT_PARAM_ERROR);
        }
        List<String> meterials = PictureUtils.uploadPicture(files,nginxProperties.getAudit(),nginxProperties.getContentUri());
        Audits audits = new Audits(userId,JsonUtil.toJson(meterials),(char)0,System.currentTimeMillis(),isManager);
        setDefaultValue(audits);
        try {
            auditDao.addAudit(audits);
            return JsonUtil.toJson(AuditResponse.UPLOADAUDITSUCCESS);
        }catch (Exception e){
            logger.warn("Add audits failed ",e);
            return JsonUtil.toJson(AuditResponse.UPLOADAUDITFAILED);
        }
    }
}
