package com.offcn.dycommon.vo;

import lombok.Data;

@Data
public class BaseVO {
    private String accessToken;//用户身份令牌（登录成功后返回给客户端）
}
