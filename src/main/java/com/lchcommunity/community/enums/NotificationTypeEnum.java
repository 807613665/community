package com.lchcommunity.community.enums;

public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");
    ;
    private int type;
    private String name;

    public static String nameOfType(Integer type) {
        for(NotificationTypeEnum i:NotificationTypeEnum.values()){
            if(i.getType()==type)
                return i.getName();
        }
        return "";
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
