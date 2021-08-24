package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.messages.MessagesDao;
import com.shiliu.dragon.model.messages.MessageResponse;
import com.shiliu.dragon.model.common.EventsType;
import com.shiliu.dragon.model.messages.Messages;
import com.shiliu.dragon.model.messages.MessagesInfo;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@RestController
@RequestMapping("/dragon/messages")
public class MessagesController {
    private static final Logger logger = LoggerFactory.getLogger(MessagesController.class);
    private static ExecutorService executors =  Executors.newCachedThreadPool();

    @Autowired
    private MessagesDao messagesDao;

    @PostMapping
    public String queryMessages(HttpServletRequest request){
        String userId = AuthUtils.getUserIdFromRequest(request);
        logger.info("Begin query {} messages",userId);
        List<Messages> messages = messagesDao.queryMessages(userId);
        if(messages.isEmpty()){
            logger.info("No new messages");
            return JsonUtil.toJson(MessageResponse.NOMESSAGES);
        }
        List<Messages> comments = messages.stream().filter(message -> message.getMessageType().equals(EventsType.COMMENT)).sorted().collect(Collectors.toList());
        List<Messages> stars = messages.stream().filter(message -> message.getMessageType().equals(EventsType.STAR)).sorted().collect(Collectors.toList());
        List<Messages> followers = messages.stream().filter(message -> message.getMessageType().equals(EventsType.FOLLOWER)).sorted().collect(Collectors.toList());
        List<Messages> collects = messages.stream().filter(message -> message.getMessageType().equals(EventsType.COLLECT)).sorted().collect(Collectors.toList());
        MessagesInfo messagesInfo = new MessagesInfo(comments,stars,followers,collects);
        MessageResponse response = MessageResponse.QUERYMESSAGESUCCESS;
        response.setMessage(messagesInfo);
        executors.submit(new Runnable() {
            @Override
            public void run() {
                messagesDao.deleteMessage(userId);
            }
        });
        return JsonUtil.toJson(response);
    }
}
