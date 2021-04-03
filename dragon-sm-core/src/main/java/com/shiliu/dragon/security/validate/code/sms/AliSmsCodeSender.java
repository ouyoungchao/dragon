package com.shiliu.dragon.security.validate.code.sms;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;

import com.google.gson.Gson;

public class AliSmsCodeSender extends DefaultSmsCodeSender {


    @Override
    public void sendSmsCode(String mobile, String code) {
        super.sendSmsCode(mobile, code);
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(mobile);
        //todo
        sendSmsRequest.setTemplateCode("");
        String  temp = String.format("短信验证码为：%s",code);
        sendSmsRequest.setTemplateParam(temp);
        try {
            SendSmsResponse smsResponse = createClient().sendSms(sendSmsRequest);
            System.out.println(new Gson().toJson(smsResponse.body));
        } catch (Exception e) {
            e.printStackTrace();
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
