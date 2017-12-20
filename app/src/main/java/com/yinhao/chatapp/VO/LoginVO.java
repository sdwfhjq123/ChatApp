package com.yinhao.chatapp.VO;

/**
 * Created by hp on 2017/12/20.
 */

public class LoginVO {
    private LoginData data;
    private String resultCode;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public class LoginData {
        private UserData user;
        private String token;

        public UserData getUser() {
            return user;
        }

        public void setUser(UserData user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public class UserData {
        private String id;
        private String nikeName;
        private String portraitUri;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNikeName() {
            return nikeName;
        }

        public void setNikeName(String nikeName) {
            this.nikeName = nikeName;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
