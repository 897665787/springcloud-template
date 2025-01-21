package com.company.admin.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.company.admin.entity.user.User;

/**
 * 基本模型
 * Created by xuxiaowei on 2017/10/23.
 */
public class BaseModel extends XSGenericModel {

    /**
     * 令牌
     */
	@TableField(exist = false)
    protected String token;

    /**
     * 当前登录用户
     */
	@TableField(exist = false)
    protected User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
