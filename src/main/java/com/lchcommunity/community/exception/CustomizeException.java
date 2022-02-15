package com.lchcommunity.community.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    private int code;

    //通过构造函数输入CustomizeErrorCode的对应code CustomizeErrorCode实现了ICustomizeErrorCode接口
    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code=errorCode.getCode();
        this.message= errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
