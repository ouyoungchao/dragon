package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.content.ContentDao;
import com.shiliu.dragon.model.content.*;
import com.shiliu.dragon.properties.NginxProperties;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.ContentInspector;
import com.shiliu.dragon.utils.PictureUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Transactional
@RestController
@RequestMapping("/dragon/content")
public class ContentController {
    private static Logger logger = LoggerFactory.getLogger(ContentController.class);


    @Autowired
    private NginxProperties nginxProperties;

    @Autowired
    private ContentDao contentDao;

    /**
     * 发布动态接口
     *
     * @param request
     * @return
     */
    @PostMapping("/publish")
    public String publishDynamic(HttpServletRequest request) {
        Content content = JsonUtil.readValue(request.getParameter("content"), Content.class);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        //图片和文字不能同时为空
        if (null == content && (files == null || files.isEmpty())) {
            logger.warn("Param is invalid {} {}", files, content);
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        if (content != null && !ContentInspector.isValidMessage(content.getMessage())) {
            return JsonUtil.toJson(ContentResponse.MESSAGE_INVALID);
        }
        if(content == null){
            content = new Content();
        }
        String userId = AuthUtils.getUserIdFromRequest(request);
        content.setUserId(userId);
        if (files != null && !files.isEmpty()) {
            content.setAnnex(PictureUtils.uploadPicture(files,nginxProperties.getContent(),nginxProperties.getContentUri()));
        }
        setDefaultValue(content);
        try {
            contentDao.addContent(content);
        } catch (Exception e) {
            logger.info("Publish content failed");
            return JsonUtil.toJson(ContentResponse.PUBLISH_FAILED);
        }
        logger.info("Publish content success");
        ContentResponse contentResponse = ContentResponse.PUBLISH_SUCCESS;
        return JsonUtil.toJson(contentResponse);
    }


    @PostMapping("/{id}")
    public String queryContentById(@PathVariable(name = "id") String id) {
        if (id.trim().isEmpty()) {
            logger.warn("Content id is invalid");
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        try {
            ContentInfo contentInfo = contentDao.queryContentById(id);
            ContentResponse contentResponse = ContentResponse.QUERYCONTENT_BYID_SUCCESS;
            if (contentResponse != null) {
                contentResponse.setMessage(contentInfo);
            }
            return JsonUtil.toJson(contentResponse);
        } catch (Exception e) {
            logger.warn("Query content by id error", e);
            return JsonUtil.toJson(ContentResponse.QUERYCONTENT_BYID_FAILED);
        }
    }

    @PostMapping("/conditionQuery")
    public String queryContents(HttpServletRequest request){
        ContentQueryModel contentQueryModel = null;
        try {
            contentQueryModel = new ContentQueryModel(request);
        }catch (IllegalArgumentException illegalArgumentException){
            logger.warn("Param error ",illegalArgumentException);
            return JsonUtil.toJson(ContentResponse.CONTENT_QUERY_PARAMERROR);
        }
        try {
            List<ContentInfo> contentInfos = contentDao.queryContentByCondition(contentQueryModel);
            ContentResponse contentResponse = ContentResponse.CONTENT_QUERY_SUCCESS;
            contentResponse.setMessage(contentInfos);
            return JsonUtil.toJson(contentResponse);
        }catch (Exception e){
            logger.warn("Query contents error ",e);
            return JsonUtil.toJson(ContentResponse.CONTENT_QUERY_FAILED);
        }
    }


    @PostMapping("/addComment")
    public String commentsContent(@RequestBody String param, HttpServletRequest request) {
        if (param == null || param.trim().isEmpty()) {
            logger.warn("Comments request param is invalid");
            return JsonUtil.toJson(ContentResponse.COMMENTS_PARAM_ERROR);
        }
        Comments comments = JsonUtil.readValue(param, Comments.class);
        if (!ContentInspector.isValidMessage(comments.getMessage()) || comments.getUserId() == null || comments.getContentId() == null) {
            logger.warn("Param is invalid");
            return JsonUtil.toJson(ContentResponse.COMMENTS_PARAM_ERROR);
        }
        setDefaultValue(comments);
        try {
            contentDao.addComments(comments);
            logger.info("Publish content success");
            ContentResponse contentResponse = ContentResponse.COMMENTS_SUCCESS;
            return JsonUtil.toJson(contentResponse);
        } catch (Exception exception) {
            logger.warn("Add comments failed ",exception);
            return JsonUtil.toJson(ContentResponse.COMMENTS_FAILED);
        }
    }

    @PostMapping("/comments/{contentId}")
    public String queryAllComments(@PathVariable(name = "contentId") String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            logger.warn("Comments request param is invalid");
            return JsonUtil.toJson(ContentResponse.COMMENTS_QUERYPARAM_ERROR);
        }
        try {
            List<Comments> comments = contentDao.queryContentAllComments(contentId);
            ContentResponse contentResponse = ContentResponse.COMMENTS_QUERYPARAM_SUCCESS;
            contentResponse.setMessage(comments);
            return JsonUtil.toJson(contentResponse);
        } catch (Exception exception) {
            logger.warn("Add comments failed");
            return JsonUtil.toJson(ContentResponse.COMMENTS_QUERYPARAM_ERROR);
        }
    }

    private void setDefaultValue(Content content) {
        content.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        content.setPublishTime(System.currentTimeMillis());
    }

    private void setDefaultValue(Comments comments) {
        comments.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        comments.setCommmentTime(System.currentTimeMillis());
    }
}
