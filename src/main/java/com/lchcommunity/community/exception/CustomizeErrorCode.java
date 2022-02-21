package com.lchcommunity.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"问题不存在或已删除！"),
    TARGET_PARAM_NOT_FOUND(2002,"未选择问题或评论"),
    NO_LOGIN(2003, "未登录不能评论，请先登录"),
    SYS_ERROR(2004,"请稍后重试~"),
    TYPE_PARAM_WRONG(2005,"评论的类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"评论不存在或已删除！"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空！"),
    ;

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
