package com.yinhao.chatapp.VO;

import java.util.List;

/**
 * Created by hp on 2017/12/21.
 */

public class GroupVO {
    private List<GroupData> data;
    private String resultCode;

    public List<GroupData> getData() {
        return data;
    }

    public void setData(List<GroupData> data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public static class GroupData {
        private String id;
        private String name;
        private String portraitUri;

        public GroupData(String id, String name, String portraitUri) {
            this.id = id;
            this.name = name;
            this.portraitUri = portraitUri;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
