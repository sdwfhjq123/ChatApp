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

    public static class FriendData {

        private String id;
        private String nikeName;
        private String portraitUri;
        private String command;


        public FriendData(String id, String nikeName, String portraitUri, String command) {
            this.id = id;
            this.nikeName = nikeName;
            this.portraitUri = portraitUri;
            this.command = command;
        }

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

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

}
