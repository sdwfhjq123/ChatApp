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

        public GroupData(String id, String name) {
            this.id = id;
            this.name = name;
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
    }
}
