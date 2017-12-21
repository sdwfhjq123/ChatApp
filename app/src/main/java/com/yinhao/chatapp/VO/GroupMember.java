package com.yinhao.chatapp.VO;

/**
 * Created by hp on 2017/12/21.
 */

public class GroupMember {
    private GroupMemberData data;
    private String resultCode;

    public GroupMemberData getData() {
        return data;
    }

    public void setData(GroupMemberData data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public class GroupMemberData {
        private String id;
        private String command;
        private String portraitUri;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

}
