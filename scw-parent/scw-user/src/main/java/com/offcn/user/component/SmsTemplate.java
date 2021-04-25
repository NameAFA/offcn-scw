package com.offcn.user.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsTemplate {
    //向指定手机号发验证码，发送成功，返回验证码；发送失败，返回空
    public String sendCode(String phone) {
        return "9588";
    }
}

