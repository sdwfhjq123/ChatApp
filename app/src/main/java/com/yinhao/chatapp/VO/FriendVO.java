package com.yinhao.chatapp.VO;

import java.util.List;

/**
 * Created by yinhao on 2017/12/20.
 */

public class FriendVO {
    private List<FriendData> data;
    private String resultCode;

    public List<FriendData> getData() {
        return data;
    }

    public void setData(List<FriendData> data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public class FriendData {
        private String portraitUri;
        private String id;
        private String nikeName;

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }

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
    }

}
