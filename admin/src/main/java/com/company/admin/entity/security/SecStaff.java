package com.company.admin.entity.security;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 系统用户
 * Created by JQ棣 on 2017/10/23.
 */
public class SecStaff extends BaseModel {

    /**
     * id
     */
    private String id;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = Save.class)
    @Pattern(regexp = "^(12[0-9]|13[0-9]|14[0-1,4-9]|15[0-9]|16[5-6]|17[0-8]|18[0-9]|19[89])\\d{8}$",
            message = "手机号格式错误", groups = {Save.class, Update.class})
    private String mobile;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "用户名长度为1-191个字符", groups = {Save.class, Update.class})
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = Save.class)
    @Length(min = 6, max = 20, message = "密码长度为6-20个字符", groups = {Save.class, Update.class})
    private String password;

    /**
     * 状态，0-冻结，1-正常
     */
    private Integer status;

    /**
     * 类型，10-超级管理员，11-员工
     */
    private Integer type;

    /**
     * 手机号区号
     */
    private String areaCode;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 真实姓名
     */
    private String nickname;

    /**
     * 角色列表
     */
    private List<SecRole> roleList;

    /**
     * 组织列表
     */
    private List<SecOrganization> organizationList;

    /**
     * 资源列表
     */
    private List<SecResource> resourceList;

    public interface Save {}

    public interface Update {}

    public SecStaff() {}

    public SecStaff(String username) {
        this.username = username;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return status.equals(1);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SecStaff) {
            return this.username.equals(((SecStaff) obj).username);
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
		return username;
	}
    
    public void setPassword(String password) {
        this.password = password;
    }

	public String getPassword() {
		return password;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<SecRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SecRole> roleList) {
        this.roleList = roleList;
    }

    public List<SecOrganization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<SecOrganization> organizationList) {
        this.organizationList = organizationList;
    }

    public List<SecResource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<SecResource> resourceList) {
        this.resourceList = resourceList;
    }

}
