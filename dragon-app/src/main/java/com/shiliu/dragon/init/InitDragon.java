package com.shiliu.dragon.init;

import com.shiliu.dragon.dao.user.UserDao;
import com.shiliu.dragon.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author ouyangchao
 * @createTime
 * @description 配置初始化参数
 */
@Component
@Order(value = 1)
public class InitDragon implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(InitDragon.class);

    private static String initUser = "admin";

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        User admin = userDao.queryUserById(initUser);
        if(admin == null){
            User user = new User();
            user.setId(initUser);
            user.setUserName(initUser);
            user.setPassword(passwordEncoder.encode(initUser));
            user.setMobile(initUser);
            userDao.addUser(user);
            logger.info("Init admin success");
        }
    }
}
