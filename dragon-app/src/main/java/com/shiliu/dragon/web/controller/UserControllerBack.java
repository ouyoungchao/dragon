package com.shiliu.dragon.web.controller;

import com.shiliu.dragon.common.utils.JsonUtil;
import com.shiliu.dragon.model.user.User;
import com.shiliu.dragon.security.validate.code.*;
import io.swagger.annotations.ApiParam;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.context.request.ServletWebRequest;

@RestController
@RequestMapping("/dragon/user1")
public class UserControllerBack {

	public static final String REGEX_PASSWORD_STRONG = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z0-9\\S]{8,}$";


/*	*//**
	 * @author Ethan
	 * @desc 查询用户信息列表
	 *//*
	@GetMapping()
	@ApiOperation(value = "查询用户信息列表")
	public List<User> query(UserQueryCondition condition,
			@PageableDefault(size=10,page=1,sort="username,asc") Pageable page){
	
		//将多个查询条件组装成一个对象
		System.out.println(ReflectionToStringBuilder.
				toString(condition,ToStringStyle.MULTI_LINE_STYLE));
		
		//将分页机制中的page相关信息打印出来
		System.out.println("size:"+page.getPageSize());
		System.out.println("num:"+page.getPageNumber());
		System.out.println("sort:"+page.getSort());

		List<User> users = new ArrayList<User>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}*/
	
	/**
	 * @author Ethan
	 * @desc 获取用户信息
	 */
	@GetMapping("/{id:\\d+}")
	public User getInfo(@ApiParam(value = "用户id") @PathVariable(name = "id") String userId){
//		throw new RuntimeException("被拦截器捕获");
//		throw new UserNotFoundException(userId);
		
		System.out.println("getInfo process");
		return new User();
	}
	
	/**
	 * @author Ethan
	 * @desc 增加用户
	 */
	@PostMapping
	public User create(@Valid @RequestBody User user,BindingResult errors){
		
		if(errors.hasErrors()){
			System.out.println("参数校验错误");
		}

		return user;
	}
	
	/**
	 * @author Ethan
	 * @desc 更新用户信息
	 */
	@PutMapping("/{id:\\d+}")
	public User update(@Valid @RequestBody User user,BindingResult errors){
		
		List<ObjectError> listError;
		if(errors.hasErrors()){
			//基于jdk8流式处理进行优化
			listError = errors.getAllErrors();
			Iterator<ObjectError> iterator = listError.iterator();
			while(iterator.hasNext()){
				System.out.println(iterator.next().getDefaultMessage());
			}
		}

		return user;
	}
	
	/**
	 * @author Ethan
	 * @desc 删除用户信息
	 */
	@DeleteMapping("/{id:\\d+}")
	public void delete(@PathVariable(name = "id") String userId){
		System.out.println("-----" + userId);
	}
	
	/**
	 * @author Ethan
	 * @desc 注册用户
	 */
	@PostMapping("/register")
	public String register(HttpServletRequest request, HttpServletResponse response, @RequestBody String userContext ){
		System.out.print("Begin register " + userContext);
		User user = JsonUtil.readValue(userContext,User.class);
		try {
			validUser(new ServletWebRequest(request),user);
		} catch (ServletRequestBindingException e) {
			System.out.println("Check smsCode ServletRequestBindingException");
			return JsonUtil.toJson(SmsResponse.SMSNOTEXIST);
		} catch (ValidateCodeException e) {
			System.out.println("Check smsCode IOException");
			return e.getMessage();
		}
		System.out.println("user detail is " + user);
		//注册用户
		return JsonUtil.toJson(UserResponse.REGISTER_SUCCESS);
	}

	/**
	 * 校验sm是否合法
	 * @param request
	 * @param user
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws ValidateCodeException
	 */
	private boolean validUser(ServletWebRequest request,User user) throws ServletRequestBindingException, ValidateCodeException {
		//请求参数值
		if(user == null){
			throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSISEMPTY));
		}
		try{
			isValidMobile(user);
			isValidSMS(request,user);
			isValidPwd(user);
			isvalidName(user);

		}catch (ValidateCodeException e){
			System.out.println(e);
			throw e;
		}
		return true;
	}

	private Boolean isvalidName(User user){
		if(StringUtils.isBlank(user.getUserName())){
			throw new ValidateCodeException(JsonUtil.toJson(UserResponse.USERNAME_ISEMPTY));
		}
		return true;
	}

	/**
	 * 校验密码是否合法
	 * @param user
	 * @return
	 * @throws ValidateCodeException
	 */
	private boolean isValidPwd(User user) throws ValidateCodeException{
		/*if(!REGEX_PASSWORD_STRONG.matches(user.getPassword())){
			throw new ValidateCodeException(JsonUtil.toJson(UserResponse.PASSWORD_RULE_NOTSATISFIED));
		}*/
		if(StringUtils.trim(user.getPassword()).length() < 8){
			throw new ValidateCodeException(JsonUtil.toJson(UserResponse.PASSWORD_RULE_NOTSATISFIED));
		}
		if(!user.getPassword().equals(user.getRepassword())){
			throw new ValidateCodeException(JsonUtil.toJson(UserResponse.PASSWORD_REPEAT));
		}
		return true;
	}

	/**
	 * 校验电话号码
	 * @param user
	 * @return
	 * @throws ValidateCodeException
	 */
	private boolean isValidMobile(User user) throws ValidateCodeException{
		if(user.getMobile()!=null && user.getMobile().length() == 13){
			return true;
		}
		throw  new ValidateCodeException(JsonUtil.toJson(UserResponse.INVALIDMOBILE));
	}

	/**
	 * 校验短信验证码
	 * @param request
	 * @param user
	 * @return
	 * @throws ValidateCodeException
	 */
	private boolean isValidSMS(ServletWebRequest request,User user) throws ValidateCodeException{
		String smsInRequest = user.getSmsCode();
		if (StringUtils.isBlank(smsInRequest)) {
			throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSISEMPTY));
		}
		if(!user.getSmsCode().equals("111111")){
			throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSUNCORRECT));
		}
		/*//系统生成值
		ValidateCode codeInSession = (ValidateCode) sessionStrategy
				.getAttribute(request, user.getMobile());
		//验证码不存在
		if (codeInSession == null) {
			throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSNOTEXIST));
		}
		//验证码已经过期
		if (codeInSession.isExpired()) {
			sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
			throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSEXPIRED));
		}
		//验证码不匹配
		if (!StringUtils.equals(codeInSession.getCode(), smsInRequest)) {
			throw new ValidateCodeException(JsonUtil.toJson(SmsResponse.SMSUNCORRECT));
		}*/
		return true;
	}
}
