package com.shiliu.dragon.controller;

import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.security.properties.UserProperties;
import com.shiliu.dragon.security.validate.AuthResponse;
import com.shiliu.dragon.utils.UserInspector;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author ouyangchao
 * @createTime
 * @description 用户登录控制器
 */

@Transactional
@RestController
@RequestMapping("/dragon/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String MOBILE = "mobile";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private Random random = new SecureRandom();

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 一键式登录，返回一个临时token
     *
     * @param request
     * @return
     */
    @PostMapping("/customer")
    public void customer(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Begin custormer login ");
        String mobile = request.getParameter(MOBILE);
        UserInspector.isValidMobile(mobile);
        User user = userDao.queryUserByMobile(mobile);
        //用户存在，则登录
        if (user != null) {
            try {
                request.getRequestDispatcher("/dragon/authentication/mobile").forward(request,response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                user = generateUser(mobile);
                userDao.addUser(user);
                request.getRequestDispatcher("/dragon/authentication/mobile").forward(request,response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("Customer success login ");
    }

    @PostMapping("/visitor")
    public void visistor(HttpServletRequest request, HttpServletResponse response) {
        //存在8600000000000的访客，则重定向到用户登录接口，不存在，则先注册，再重定向到登录接口
        logger.info("Begin visitor login ");
        User user = userDao.queryUserByMobile(UserProperties.VISITOR);
        //用户存在，则登录
        if (user != null) {
            response.addHeader(UserProperties.CUSTOMERTAG,"true");
            try {
                request.getRequestDispatcher("/dragon/authentication/user").forward(request,response);
            } catch (ServletException | IOException e) {
                logger.error("Customer visistor error ",e);
            }
        } else {
            try {
                user = generateUser(UserProperties.VISITOR,UserProperties.VISITOR);
                userDao.addUser(user);
                request.getRequestDispatcher("/dragon/authentication/user").forward(request,response);
            } catch (ServletException | IOException e) {
                logger.error("Customer visistor error ",e);
            }
        }
        logger.info("Visitor success login ");
    }

    private User generateUser(String mobile) {
        String pwd = getRandomPwd(8);
        User user = new User(mobile,pwd,pwd,"china",getRandomName(random.nextInt(5)+2),"null","null",(byte)0);
        setDefaultValue(user);
        return user;
    }

    private User generateUser(String mobile,String pwd) {
        User user = new User(mobile,pwd,pwd,"china",getRandomName(random.nextInt(5)+2),"null","null",(byte)0);
        setDefaultValue(user);
        return user;
    }

    private void setDefaultValue(User user) {
        if (user.getDescription() == null) {
            user.setDescription(User.DEFAULT_DESCRIPTION);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        user.setRegisterTime(System.currentTimeMillis());
    }

    public String getRandomPwd(int length){
        char[] chars = new char[length];
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            chars[i] = (char) ('0' + rnd.nextInt(10));
        }
        return new String(chars);
    }

    public String getRandomName(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();

            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                logger.warn("Get random name error",ex);
            }
            ret += str;
        }
        return ret;
    }


}
