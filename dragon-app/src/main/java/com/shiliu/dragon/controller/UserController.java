package com.shiliu.dragon.controller;

import com.shiliu.dragon.common.AuthUtils;
import com.shiliu.dragon.common.UserInspector;
import com.shiliu.dragon.common.utils.JsonUtil;
import com.shiliu.dragon.dao.UserDao;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.model.user.UserModifyModel;
import com.shiliu.dragon.model.user.UserQueryModel;
import com.shiliu.dragon.security.validate.code.SmsResponse;
import com.shiliu.dragon.security.validate.code.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

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

    @PostMapping("/register")
    public String register(@RequestBody String userContext) {
        logger.info("Begin register " + userContext);
        User user = JsonUtil.readValue(userContext, User.class);
        try {
            UserInspector.validUser(user);
        } catch (ServletRequestBindingException e) {
            logger.error("Check smsCode ServletRequestBindingException ", e);
            return JsonUtil.toJson(SmsResponse.SMSNOTEXIST);
        } catch (ValidateCodeException e) {
            logger.error("Check smsCode IOException ", e);
            return e.getMessage();
        }
        if(userDao.queryUserByMobile(user.getMobile()) != null){
            logger.error("The mobile has register");
            return JsonUtil.toJson(UserResponse.MOBILE_REGISTED);
        }
        userDao.addUser(user);
        logger.info("Add user {} success", user.getMobile());
        //注册用户
        return JsonUtil.toJson(UserResponse.REGISTER_SUCCESS);
    }

    @GetMapping("/{id}")
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
     * @param request
     * @return
     */
    @GetMapping("/conditionQuery")
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
        return JsonUtil.toJson(userResponse);
    }

    @GetMapping
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
    public String modifyUser(@RequestBody String userInfo,HttpServletRequest request){
        logger.info("Begin modify user {}",userInfo);
        UserModifyModel userModifyModel = JsonUtil.readValue(userInfo,UserModifyModel.class);
        if(userModifyModel == null){
            return JsonUtil.toJson(UserResponse.INVALIDPARAM);
        }
        userModifyModel.setId(AuthUtils.getUserIdFromRequest(request));
        try {
            userModifyModel.isValidFilders();
        }catch (ValidateCodeException e){
            logger.error("ModifyUser with ValidateCodeException",e);
            return e.getMessage();
        }
        userDao.updateUser(userModifyModel);
        return JsonUtil.toJson(UserResponse.Modify_SUCCESS);

    }
}
