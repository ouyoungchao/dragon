package com.shiliu.dragon.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shiliu.dragon.security.validate.*;
import com.shiliu.dragon.utils.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.shiliu.dragon.security.properties.SecurityProperties;
import com.shiliu.dragon.security.validate.sms.SmsCodeSender;

@RestController
public class CodeController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private ValidateCodeGenerator imageCodeGenerator;

	@Autowired
	private ValidateCodeGenerator smsCodeGenerator;
	
	@Autowired
	private SmsCodeSender smsCodeSender;

	@Autowired
	private RedisTemplate redisTemplate;

	@GetMapping("/dragon/code/image")
	public void create(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//生成验证码
		ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(request);
		//保存到session中
		redisTemplate.opsForValue().set(SESSION_KEY, imageCode);
		//输出到前台
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}
	
	@GetMapping("/dragon/code/sms")
	public String createSmsCode(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletRequestBindingException{
		//不同的短信供应商，声明为不同接口
		String mobile = ServletRequestUtils.getStringParameter(request, "mobile");
		if(mobile == null || mobile.trim().length() != 13){
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return JsonUtil.toJson(SmsResponse.INVALIDPARAM);
		}
		//遍历所有打国家码，查看是否支持
		boolean supportCountry = false;
		for(COUNTRYCODE countrycode : COUNTRYCODE.values()){
			if(mobile.startsWith(COUNTRYCODE.CHINA.getCode())){
				supportCountry = true;
				break;
			}
		}
		if(!supportCountry){
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return JsonUtil.toJson(SmsResponse.UNSUPPORTCOUNTRY);
		}
		//生成验证码
		ValidateCode smsCode = smsCodeGenerator.generate(request);
		ServletWebRequest servletWebRequest  = new ServletWebRequest(request);
		String authenticMobile = mobile.substring(2);
		if(smsCodeSender.sendSmsCode(authenticMobile, smsCode.getCode())) {
			//保存到session中
			logger.info("smscode = {} and moble = {} request = ",smsCode,mobile,servletWebRequest.hashCode());
			redisTemplate.opsForValue().set(mobile,smsCode,3, TimeUnit.MINUTES);
			return JsonUtil.toJson(SmsResponse.SUCCESS);
		}else{
			return JsonUtil.toJson(SmsResponse.FAILED);
		}
	}
}
