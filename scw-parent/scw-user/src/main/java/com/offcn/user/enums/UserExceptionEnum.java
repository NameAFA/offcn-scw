package com.offcn.user.enums;

public enum UserExceptionEnum {
    LOGINACCT_EXIST(1, "登录账号（手机号）已经存在"),
    EMAIL_EXIST(2, "邮箱已经存在"),
    LOGINACCT_LOCKED(3, "账号已经被锁定");

    private int code;
    private String msg;

    UserExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
