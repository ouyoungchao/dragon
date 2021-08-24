package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.fans.FansDao;
import com.shiliu.dragon.dao.messages.MessagesDao;
import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.fans.Fans;
import com.shiliu.dragon.model.fans.FansResponse;
import com.shiliu.dragon.model.common.EventsType;
import com.shiliu.dragon.model.messages.Messages;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.random.RandomUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private static ExecutorService executors =  Executors.newCachedThreadPool();

    @Autowired
    private UserDao userDao;

    @Autowired
    private FansDao fansDao;

    @Autowired
    private MessagesDao messagesDao;

    @PostMapping("follow")
    public String follower(HttpServletRequest request) {
        logger.info("Begin follow");
        String userId = request.getParameter(USER_ID);
        if(StringUtils.isEmpty(userId)){
            logger.warn("User id  {} error",userId);
            return JsonUtil.toJson(FansResponse.FANS_PARAM_ERROR);
        }
        User uper = userDao.queryUserById(userId);
        if (uper == null) {
            logger.warn("User not exist");
            return JsonUtil.toJson(FansResponse.FANS_USER_NOTEXIT);
        }
        String follow = AuthUtils.getUserIdFromRequest(request);
        Fans fans = new Fans(RandomUtils.getDefaultRandom(),uper.getId(),follow,System.currentTimeMillis());
        fansDao.addFans(fans);
        addMessages(uper.getId(),follow, EventsType.FOLLOWER);
        return JsonUtil.toJson(FansResponse.FANS_FOLLOW_SUCCESS);
    }

    @PostMapping("cancelFollow")
    public String cancelFollower(HttpServletRequest request) {
        logger.info("Begin cancel follow");
        String userId = request.getParameter(USER_ID);
        if(StringUtils.isEmpty(userId)){
            logger.warn("User id  {} error",userId);
            return JsonUtil.toJson(FansResponse.FANS_PARAM_ERROR);
        }
        String follow = AuthUtils.getUserIdFromRequest(request);
        Fans fans = new Fans(RandomUtils.getDefaultRandom(),userId,follow,System.currentTimeMillis());
        fansDao.deleteFans(fans);
        deleteMessages(follow,userId, EventsType.FOLLOWER);
        return JsonUtil.toJson(FansResponse.FANS_CANCEL_FOLLOW_SUCCESS);
    }

    @PostMapping("queryFans")
    public String queryFans(HttpServletRequest request){
        logger.info("Begin query fans");
        String upper = AuthUtils.getUserIdFromRequest(request);
        if(StringUtils.isEmpty(upper)){
            logger.warn("User id  {} error",upper);
            return JsonUtil.toJson(FansResponse.FANS_PARAM_ERROR);
        }
        List<Fans> fans = fansDao.queryFans(upper);
        FansResponse fansResponse = FansResponse.FANS_QUERY_SUCCESS;
        fansResponse.setMessage(fans);
        return JsonUtil.toJson(fansResponse);

    }

    private void addMessages(String userId, String relatedUserId, EventsType eventsType) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                User user = userDao.queryUserById(relatedUserId);
                String relatedUserName = user.getUserName();
                String relatedUserPortrait = user.getPortrait();
                long productedTime = System.currentTimeMillis();
                String id = RandomUtils.getDefaultRandom();
                Messages messages = new Messages(id,userId,relatedUserName,relatedUserPortrait,relatedUserId,"","",productedTime, eventsType);
                messagesDao.addMessages(messages);
            }
        });
    }

    private void deleteMessages(String userId, String relatedUserId, EventsType eventsType) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                messagesDao.deleteMessages(userId,relatedUserId, eventsType);
            }
        });
    }
}
