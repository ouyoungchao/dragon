package com.shiliu.dragon.security.validate.code.sms;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliSmsCodeSender extends DefaultSmsCodeSender {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendSmsCode(String mobile, String code) {
        logger.info("Begin send sms code to mobile " + mobile);
        super.sendSmsCode(mobile, code);
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(mobile);
        //todo
        sendSmsRequest.setTemplateCode("");
        String  temp = String.format("短信验证码为：{}",code);
        sendSmsRequest.setTemplateParam(temp);
        try {
            SendSmsResponse smsResponse = createClient().sendSms(sendSmsRequest);
           logger.info("Send sms code success " + new Gson().toJson(smsResponse.body));
        } catch (Exception e) {
            logger.error("Send sms code error ",e);
        }


    }


    public static Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(smsCodeProperties.getAccessKey())
                // 您的AccessKey Secret
                .setAccessKeySecret(smsCodeProperties.getAccessKeySecret());
        // 访问的域名
        config.endpoint = smsCodeProperties.getUrl();
        return new Client(config);
    }
}
