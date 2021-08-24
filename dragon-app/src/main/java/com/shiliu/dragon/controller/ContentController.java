package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.content.ContentDao;
import com.shiliu.dragon.dao.messages.MessagesDao;
import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.content.*;
import com.shiliu.dragon.model.exception.DragonException;
import com.shiliu.dragon.model.common.EventsType;
import com.shiliu.dragon.model.messages.Messages;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.properties.NginxProperties;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.ContentInspector;
import com.shiliu.dragon.utils.PictureUtils;
import com.shiliu.dragon.utils.random.RandomUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Transactional
@RestController
@RequestMapping("/dragon/content")
public class ContentController {
    public static final String COLLECTIONS = "collections";
    private static Logger logger = LoggerFactory.getLogger(ContentController.class);
    private static final String CONTENT_ID = "contentId";
    private static final String COMMENT_ID = "commentId";

    private static ExecutorService execute = Executors.newCachedThreadPool();

    @Autowired
    private NginxProperties nginxProperties;

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessagesDao messagesDao;

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
        if (content == null) {
            content = new Content();
        }
        content.setSubject(request.getParameter("subject"));
        String userId = AuthUtils.getUserIdFromRequest(request);
        content.setUserId(userId);
        if (files != null && !files.isEmpty()) {
            content.setAnnex(PictureUtils.uploadPicture(files, nginxProperties.getContent(), nginxProperties.getContentUri()));
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

    @DeleteMapping("/delete")
    public String deleteContent(HttpServletRequest request) {
        logger.info("Begin delete content");
        String contentId = request.getParameter(CONTENT_ID);
        if (StringUtils.isEmpty(contentId)) {
            logger.warn("Content param error {}", contentId);
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        Content content = contentDao.queryContent(contentId);
        if (content == null) {
            logger.warn("Content {} not exit", contentId);
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        contentDao.deleteContent(contentId);
        return JsonUtil.toJson(ContentResponse.DELETE_SUCCESS);
    }

    @DeleteMapping("/deleteComment")
    public String deleteComment(HttpServletRequest request) {
        logger.info("Begin delete comment");
        String commentId = request.getParameter(COMMENT_ID);
        if (StringUtils.isEmpty(commentId)) {
            logger.warn("Content param error {}", commentId);
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        Comments comments = contentDao.queryComment(commentId);
        if (comments == null) {
            logger.warn("Content {} not exit", comments);
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        contentDao.deleteCommentById(commentId);
        return JsonUtil.toJson(ContentResponse.COMMENTS_DELETE_SUCCESS);
    }


    @PostMapping("/details/{id}")
    public String queryContentById(@PathVariable(name = "id") String id) {
        if (id.trim().isEmpty()) {
            logger.warn("Content id is invalid");
            return JsonUtil.toJson(ContentResponse.CONTENT_PARAM_ERROR);
        }
        try {
            ContentInfo contentInfo = contentDao.queryContentInfoById(id);
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

    /**
     * 条件查询所有贴子
     *
     * @param request
     * @return
     */
    @PostMapping("/conditionQuery")
    public String queryContents(HttpServletRequest request) {
        ContentQueryModel contentQueryModel = null;
        try {
            contentQueryModel = new ContentQueryModel(request);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.warn("Param error ", illegalArgumentException);
            return JsonUtil.toJson(ContentResponse.CONTENT_QUERY_PARAMERROR);
        }
        try {
            List<ContentInfo> contentInfos = contentDao.queryContentByCondition(contentQueryModel);
            ContentResponse contentResponse = ContentResponse.CONTENT_QUERY_SUCCESS;
            contentResponse.setMessage(contentInfos);
            return JsonUtil.toJson(contentResponse);
        } catch (Exception e) {
            logger.warn("Query contents error ", e);
            return JsonUtil.toJson(ContentResponse.CONTENT_QUERY_FAILED);
        }
    }


    /**
     * 添加评论
     *
     * @param param
     * @param request
     * @return
     */
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
        Object content;
        if (!comments.isIsComment()) {
            content = contentDao.queryContent(comments.getContentId());
        } else {
            content = contentDao.queryComment(comments.getContentId());
        }
        if (content == null) {
            logger.error("content {} not exist ", comments.getContentId());
            return JsonUtil.toJson(ContentResponse.COMMENTS_CONTENT_NOT_EXIST);
        }
        User user = userDao.queryUserById(comments.getUserId());
        setComentUserInfo(comments, user);
        setDefaultValue(comments);
        try {
            contentDao.addComments(comments);
            logger.info("Publish content success");
            addMessages(comments.getContentId(), comments.getUserId(), EventsType.COMMENT);
            ContentResponse contentResponse = ContentResponse.COMMENTS_SUCCESS;
            return JsonUtil.toJson(contentResponse);
        } catch (Exception exception) {
            logger.warn("Add comments failed ", exception);
            return JsonUtil.toJson(ContentResponse.COMMENTS_FAILED);
        }
    }

    private void setComentUserInfo(Comments comments, User user) {
        comments.setUserName(user.getUserName());
        comments.setUserPortrait(user.getPortrait());
    }

    /**
     * 查询某个帖子的所有评论
     *
     * @param contentId
     * @return
     */
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

    /**
     * 点赞
     *
     * @param request
     * @return
     */
    @PostMapping("/star")
    public String star(HttpServletRequest request) {
        logger.info("Begin star");
        String contentId = request.getParameter(CONTENT_ID);
        if (StringUtils.isEmpty(contentId)) {
            logger.warn("Star param error {}", contentId);
            return JsonUtil.toJson(ContentResponse.STAR_PARAMERROR);
        }
        Content content = contentDao.queryContent(contentId);
        if (content == null) {
            logger.warn("Content not exist {}", contentId);
            return JsonUtil.toJson(ContentResponse.STAR_CONTENTID_NOTEXIST);
        }
        String userId = AuthUtils.getUserIdFromRequest(request);
        Star star = new Star();
        star.setContentId(contentId);
        star.setUserId(userId);
        setDefaultValue(star);
        contentDao.addEvent(star);
        addMessages(contentId, AuthUtils.getUserIdFromRequest(request), EventsType.STAR);
        return JsonUtil.toJson(ContentResponse.STAR_SUCCESS);
    }

    private void setDefaultValue(ContentEvents contentEvents) {
        User user = userDao.queryUserById(contentEvents.getUserId());
        contentEvents.setUserName(user.getUserName());
        contentEvents.setUserPortrait(user.getPortrait());
        contentEvents.setId(RandomUtils.getDefaultRandom());
        contentEvents.setAccurrentTime(System.currentTimeMillis());
    }

    @PostMapping("/cancelStar")
    public String cancelStar(HttpServletRequest request) {
        logger.info("Begin cancel star");
        String contentId = request.getParameter(CONTENT_ID);
        if (StringUtils.isEmpty(contentId)) {
            logger.warn("Star param error {}", contentId);
            return JsonUtil.toJson(ContentResponse.STAR_PARAMERROR);
        }
        Content content = contentDao.queryContent(contentId);
        if (content == null) {
            logger.warn("Content not exist {}", contentId);
            return JsonUtil.toJson(ContentResponse.STAR_CONTENTID_NOTEXIST);
        }
        String userId = AuthUtils.getUserIdFromRequest(request);
        contentDao.cancelEvent(userId, contentId, EventsType.STAR);
        deleteMessages(contentId, userId, EventsType.STAR);
        return JsonUtil.toJson(ContentResponse.STAR_CANCEL_SUCCESS);
    }

    /**
     * 收藏
     *
     * @param request
     * @return
     * @throws DragonException
     */
    @PostMapping("/collect")
    public String collectContent(HttpServletRequest request) throws DragonException {
        logger.info("Begin to collect");
        String contentId = request.getParameter(CONTENT_ID);
        if (StringUtils.isEmpty(contentId)) {
            logger.warn("Collect param error {}", contentId);
            return JsonUtil.toJson(ContentResponse.COLLECT_PARAMERROR);
        }
        if (contentDao.queryContentInfoById(contentId) == null) {
            return JsonUtil.toJson(ContentResponse.COLLECT_CONTENTID_NOTEXIST);
        }
        String userId = AuthUtils.getUserIdFromRequest(request);
        ContentEvents collect = contentDao.queryUserEvent(contentId, userId, EventsType.COLLECT);
        if (collect != null) {
            logger.warn("User {} has Collected content {}", userId, contentId);
        } else {
            collect = new Collect();
            collect.setContentId(contentId);
            collect.setUserId(userId);
            setDefaultValue(collect);
            contentDao.addEvent(collect);
            logger.info("{} Collect {} success", userId, contentId);

        }
        addMessages(contentId, AuthUtils.getUserIdFromRequest(request), EventsType.COLLECT);
        return JsonUtil.toJson(ContentResponse.COLLECT_SUCCESS);
    }


    @PostMapping("/cancelCollect")
    public String cancelConllect(HttpServletRequest request) throws DragonException {
        logger.info("Begin to cancel collect");
        String contentId = request.getParameter(CONTENT_ID);
        if (StringUtils.isEmpty(contentId)) {
            logger.warn("Collect param error {}", contentId);
            return JsonUtil.toJson(ContentResponse.COLLECT_PARAMERROR);
        }
        if (contentDao.queryContentInfoById(contentId) == null) {
            return JsonUtil.toJson(ContentResponse.COLLECT_CONTENTID_NOTEXIST);
        }
        String userId = AuthUtils.getUserIdFromRequest(request);
        ContentEvents contentEvents = contentDao.queryUserEvent(contentId, userId, EventsType.COLLECT);
        if (contentEvents != null) {
            contentDao.cancelEvent(userId, contentId, EventsType.COLLECT);
            logger.info("{} cancel Collect {}} success", userId, contentId);
            deleteMessages(contentId, userId, EventsType.COLLECT);
        }
        return JsonUtil.toJson(ContentResponse.COLLECT_CANCEL_SUCCESS);
    }

    @PostMapping("/myCollections")
    public String myCollections(HttpServletRequest request) {
        String userId = AuthUtils.getUserIdFromRequest(request);
        List<ContentEvents> collects = contentDao.queryUserEventS(userId, EventsType.COLLECT);
        if (collects != null) {
            ContentResponse response = ContentResponse.QUERY_MYCOLLECTION_SUCCESS;
            List<ContentInfo> contentInfos = new ArrayList<>();
            for (ContentEvents event : collects) {
                contentInfos.add(contentDao.queryContentInfoById(event.getContentId()));
            }
            response.setMessage(contentInfos);
            return JsonUtil.toJson(response);
        } else {
            ContentResponse response = ContentResponse.QUERY_MYCOLLECTION_EMPTY;
            return JsonUtil.toJson(response);
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

    /**
     * @param contentId     点赞/评论/收藏者的内容的id
     * @param relatedUserId 点赞/评论/收藏者的id
     */
    private void addMessages(String contentId, String relatedUserId, EventsType eventsType) {
        execute.submit(new Runnable() {
            @Override
            public void run() {
                User user = userDao.queryUserById(relatedUserId);
                String relatedUserName = user.getUserName();
                String relatedUserPortrait = user.getPortrait();
                Content content = contentDao.queryContent(contentId);
                String userId = content.getUserId();
                long productedTime = System.currentTimeMillis();
                String message = content.getMessage();
                String id = RandomUtils.getDefaultRandom();
                Messages messages = new Messages(id, userId, relatedUserName, relatedUserPortrait, relatedUserId, message, contentId, productedTime, eventsType);
                messagesDao.addMessages(messages);
            }
        });
    }

    private void deleteMessages(String cotentId, String relatedUserId, EventsType eventsType) {
        execute.submit(new Runnable() {
            @Override
            public void run() {
                Content content = contentDao.queryContent(cotentId);
                if (content != null) {
                    String userId = content.getUserId();
                    messagesDao.deleteMessages(userId, relatedUserId, eventsType);
                }
            }
        });
    }
}
