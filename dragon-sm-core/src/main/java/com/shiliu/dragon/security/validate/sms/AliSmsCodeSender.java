package com.shiliu.dragon.security.validate.sms;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AliSmsCodeSender extends DefaultSmsCodeSender {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static Client smsClient;

    @Override
    public boolean sendSmsCode(String mobile, String code) {
        logger.info("Begin send sms code to mobile " + mobile);
        super.sendSmsCode(mobile, code);
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(mobile);
        sendSmsRequest.setTemplateCode(smsCodeProperties.getTemplateParam(mobile));
        sendSmsRequest.setSignName(smsCodeProperties.getSignName(mobile));
        String  temp = String.format("{\"code\":\""+ code +"\"}");
        sendSmsRequest.setTemplateParam(temp);
        try {
            SendSmsResponse smsResponse = createClient().sendSms(sendSmsRequest);
           logger.info("Send sms code success " + new Gson().toJson(smsResponse.body));
           return true;
        } catch (Exception e) {
            logger.error("Send sms code error ",e);
            return false;
        }


    }


    public synchronized static Client createClient() throws Exception {
        if(smsClient == null) {
            Config config = new Config()
                    // 您的AccessKey ID
                    .setAccessKeyId(smsCodeProperties.getAk())
                    // 您的AccessKey Secret
                    .setAccessKeySecret(smsCodeProperties.getSk());
            // 访问的域名
            config.endpoint = smsCodeProperties.getUrl();
            smsClient = new Client(config);
        }
        return smsClient;
    }
}
