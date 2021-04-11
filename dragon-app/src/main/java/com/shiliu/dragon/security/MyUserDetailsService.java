package com.shiliu.dragon.security;

import com.shiliu.dragon.dao.UserDao;
import com.shiliu.dragon.model.user.DragonSocialUser;
import com.shiliu.dragon.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService,SocialUserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//表单登录时，根据用户名查找用户信息
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		logger.info("登陆用户"+username);
		//new Exception("user login").printStackTrace();
		return buildUser(username);

	}

	//社交登录时，根据用户名查找用户信息
	public SocialUserDetails loadUserByUserId(String userId)
			throws UsernameNotFoundException {
		logger.info("社交登陆用户"+userId);
		return buildUser(userId);

	}

	private SocialUserDetails buildUser(String userId) {

		if(userId==null || userId.trim().isEmpty()){
			return null;
		}
		//根据数据库查询username的密码和授权
		//根据数据库查找到的信息判断用户是否被冻结
		//最后返回一个UserDetails的实例即可
		// TODO: 2021/3/22
		User user = userDao.queryUserByMobile(userId);
		String password = passwordEncoder.encode(user.getPassword());
		//match匹配password和前台输入的密码
		try {
			SocialUser socialUser =  new SocialUser(user.getId(),password,
					true,true,true,true,
					//完成用户的授权
					AuthorityUtils.commaSeparatedStringToAuthorityList(user.getUserName()));
//			SocialUser socialUser = new DragonSocialUser(user.getId(),user.getUserName(),user.getPassword(),"Dragon",AuthorityUtils.commaSeparatedStringToAuthorityList("Dragon"),true,true,true,true);
			return socialUser;
		}catch (Exception e){
			logger.warn("BuildUser error");
			throw e;
		}

	}
}
