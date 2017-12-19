package com.yinhao.chatapp.model;

/**
 * Created by hp on 2017/12/18.
 */

public class Friends {
    private String userId;
    private String userName;
    private String portraitUri;

    public Friends(String userId, String userName, String portraitUri) {
        this.userId = userId;
        this.userName = userName;
        this.portraitUri = portraitUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }
}
