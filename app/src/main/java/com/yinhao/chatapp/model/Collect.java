package com.yinhao.chatapp.model;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by yinhao on 2017/12/24.
 */

public class Collect extends DataSupport {

    private int id;
    private String url;
    private Date createTime;
    //0为文本 1为pic
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
