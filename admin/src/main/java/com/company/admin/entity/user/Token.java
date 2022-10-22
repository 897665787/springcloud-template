package com.company.admin.entity.user;

import com.company.admin.entity.base.BaseModel;

/**
 * 令牌
 * Created by xuxiaowei on 2017/10/26.
 */
public class Token extends BaseModel {

    /**
     * id
     */
    private String id;

    public Token() {}

    public Token(String id) {
        this.id = id;
    }

    public Token(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
