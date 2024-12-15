package com.company.system.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class SysUserInfoResp {

    User user;

    Set<String> permissions;

    @Data
    public static class User {
        /**
         * id
         */
        private Integer id;

        /**
         * 账号
         */
        private String account;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 用户邮箱
         */
        private String email;

        /**
         * 手机号码
         */
        private String phonenumber;

        /**
         * 用户性别(0男 1女 2未知)
         */
        private String sex;

        /**
         * 头像地址
         */
        private String avatar;

        /**
         * 帐号状态(ON:正常,OFF:停用)
         */
        private String status;

        /**
         * 部门ID
         */
        private Integer deptId;

        /**
         * 用户备注
         */
        private String userRemark;
    }
}
