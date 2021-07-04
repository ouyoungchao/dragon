package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.log.LoggerDao;
import com.shiliu.dragon.model.user.*;
import com.shiliu.dragon.properties.NginxProperties;
import com.shiliu.dragon.utils.AuthUtils;
import com.shiliu.dragon.utils.UserInspector;
import com.shiliu.dragon.utils.utils.JsonUtil;
import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.security.validate.SmsResponse;
import com.shiliu.dragon.security.validate.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */

@Transactional
@RestController
@RequestMapping("/dragon/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoggerDao loggerDao;

    @Autowired
    private NginxProperties nginxProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //注册用户接口
    @PostMapping("/register")
    public String register(@RequestBody String userContext) {
        logger.info("Begin register " + userContext);
        User user = JsonUtil.readValue(userContext, User.class);
        try {
            UserInspector.validUser(user,redisTemplate);
            setDefaultValue(user);
        } catch (ServletRequestBindingException e) {
            logger.error("Check smsCode ServletRequestBindingException ", e);
            return JsonUtil.toJson(SmsResponse.SMSNOTEXIST);
        } catch (ValidateCodeException e) {
            logger.error("Check smsCode IOException ", e);
            return e.getMessage();
        }
        if (userDao.queryUserByMobile(user.getMobile()) != null) {
            logger.error("The mobile has register");
            return JsonUtil.toJson(UserResponse.MOBILE_REGISTED);
        }
        userDao.addUser(user);
        logger.info("Add user {} success", user.getMobile());
        //注册用户
        return JsonUtil.toJson(UserResponse.REGISTER_SUCCESS);
    }

    private void setDefaultValue(User user) {
        if (user.getDescription() == null) {
            user.setDescription(User.DEFAULT_DESCRIPTION);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        user.setRegisterTime(System.currentTimeMillis());
    }

    @PostMapping("/{id}")
    public String queryUserById(@PathVariable(name = "id") String id) {
        try {
            logger.info("begin query user " + id);
            UserInspector.isValidUserId(id);
            User user = userDao.queryUserById(id);
            if (user != null) {
                UserResponse userResponse = UserResponse.QUERY_USER_SUCCESS;
                userResponse.setMessage(user);
                logger.info("Query user {} success", id);
                return JsonUtil.toJson(userResponse);
            }
            logger.warn("User {} not exit", id);
        } catch (ValidateCodeException e) {
            logger.error("Quesy user with ValidateCodeException ", e);
        }
        return JsonUtil.toJson(UserResponse.USER_NOT_EXIST);
    }


    /**
     * 条件查询接口
     *
     * @param request
     * @return
     */
    @PostMapping("/conditionQuery")
    public String conditionQuery(HttpServletRequest request) {
        logger.info("Begin conditionQuery");
        UserQueryModel userQueryModel = new UserQueryModel(request);
        List<User> users = userDao.conditionQuery(userQueryModel);
        UserResponse userResponse = UserResponse.QUERY_USER_SUCCESS;
        if (users == null || users.isEmpty()) {
            logger.warn("Users meet the condition is empty");
            userResponse.setMessage(Collections.EMPTY_LIST);
        } else {

            userResponse.setMessage(users);
        }
        String result = JsonUtil.toJson(userResponse);
        logger.info("result {} ", result);
        return result;
    }

    @PostMapping
    public String query(HttpServletRequest request) {
        int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
        int limit = request.getParameter("pageSize") == null ? 10 : Integer.parseInt(request.getParameter("pageSize"));
        logger.info("Begin quesy user offset {} pageSize {}", offset, limit);
        List<User> users = userDao.queryUsers(offset, limit);
        UserResponse userResponse = UserResponse.QUERY_USER_SUCCESS;
        if (users == null || users.isEmpty()) {
            logger.warn("Users is empty");
            userResponse.setMessage(Collections.EMPTY_LIST);
        } else {

            userResponse.setMessage(users);
        }
        return JsonUtil.toJson(userResponse);
    }

    @PostMapping("/modify")
    public String modifyUser(@RequestBody String userInfo, HttpServletRequest request) {
        logger.info("Begin modify user {}", userInfo);
        UserModifyModel userModifyModel = JsonUtil.readValue(userInfo, UserModifyModel.class);
        if (userModifyModel == null) {
            return JsonUtil.toJson(UserResponse.INVALIDPARAM);
        }
        userModifyModel.setId(AuthUtils.getUserIdFromRequest(request));
        try {
            userModifyModel.isValidFilders(passwordEncoder);
        } catch (ValidateCodeException e) {
            logger.error("ModifyUser with ValidateCodeException", e);
            return e.getMessage();
        }
        userDao.updateUser(userModifyModel);
        return JsonUtil.toJson(UserResponse.Modify_SUCCESS);

    }

    @PostMapping("/portrait")
    public String uploadPortrait(MultipartFile file, HttpServletRequest request) throws Exception {
        if (file == null || !(file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png"))) {
            logger.warn("Upload file is error {}", file);
            return JsonUtil.toJson(UserResponse.INVALIDPARAM);
        }
        logger.info("Begin upload file " + file.getOriginalFilename());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
        String uploadPath = nginxProperties.getPortrait();
        File localFile = new File(uploadPath, new Date().getTime() + suffix);
        String id = AuthUtils.getUserIdFromRequest(request);
        if (id == null) {
            logger.warn("userId is null");
            return JsonUtil.toJson(UserResponse.INVALIDPARAM);
        }
        String value = null;
        //查询是否已经存在头像
        Map userExtends = userDao.queryUserPortrait(id, User.PORTRAITURI_NAME);
        if (userExtends != null && !userExtends.isEmpty() && !userExtends.get(User.PORTRAITURI_NAME).toString().endsWith(User.PORTRAITURI_DEFAULT_VALUE)) {
            logger.info("User portrait has existed");
            String uri = ((String) userExtends.get(User.PORTRAITURI_NAME));
            String fileName = uri.substring(uri.lastIndexOf("/"));
            localFile = new File(uploadPath + fileName);
            file.transferTo(localFile);
            value = nginxProperties.getPortraitUri() + localFile.getName();
            try {
                userDao.updateUserPortrait(id, User.PORTRAITURI_NAME, value);
            } catch (Exception e) {
                logger.warn("Update portrait failed ", e);
                return JsonUtil.toJson(UserResponse.MODIFY_PORTRAIT_FAILED);
            }
            UserResponse userResponse = UserResponse.MODIFY_PORTRAIT_SUCCESS;
            Map<String, String> result = new HashMap<>();
            result.put(User.PORTRAITURI_NAME, value);
            userResponse.setMessage(result);
            logger.info("Upload portrait success");
            return JsonUtil.toJson(userResponse);
        } else {
            localFile.createNewFile();
            //将传输内容进行转换
            file.transferTo(localFile);
            value = nginxProperties.getPortraitUri() + localFile.getName();
            try {
                userDao.addUserPortrait(id, User.PORTRAITURI_NAME, value);
            } catch (Exception e) {
                logger.warn("Upload portrait failed");
                return JsonUtil.toJson(UserResponse.UPLOAD_PORTRAIT_FAILED);
            }
            UserResponse userResponse = UserResponse.UPLOAD_PORTRAIT_SUCCESS;
            Map<String, String> result = new HashMap<>();
            result.put(User.PORTRAITURI_NAME, value);
            userResponse.setMessage(result);
            logger.info("Upload portrait success");
            return JsonUtil.toJson(userResponse);
        }

    }

    @PostMapping("/hotUsers")
    public String getPopularUser(HttpServletRequest request){
        HotUserModule userQueryModel = new HotUserModule(request);
        List<User> users = loggerDao.queryHotUser(userQueryModel);
        UserResponse userResponse = UserResponse.QUERY_USER_SUCCESS;
        if (users == null || users.isEmpty()) {
            logger.warn("Users meet the condition is empty");
            userResponse.setMessage(Collections.EMPTY_LIST);
        } else {
            userResponse.setMessage(users);
        }
        String result = JsonUtil.toJson(userResponse);
        logger.info("result {} ", result);
        return result;
    }
}
