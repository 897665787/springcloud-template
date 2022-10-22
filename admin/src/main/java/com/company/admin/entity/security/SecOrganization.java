package com.company.admin.entity.security;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.tree.XSTreeable;

/**
 * 系统组织
 * Created by xuxiaowei on 2017/10/23.
 */
public class SecOrganization extends BaseModel implements XSTreeable<Long, SecOrganization> {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "名称长度为1-191个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 父级
     */
    private SecOrganization parent;

    /**
     * 状态，0禁用，1正常
     */
    private Integer status;

    /**
     * 描述
     */
    @Length(max = 255, message = "描述长度最多为255个字符", groups = {Save.class, Update.class})
    private String desc;

    /**
     * 子级列表
     */
    private List<SecOrganization> children;

    /**
     * 组织列表
     */
    private List<SecOrganization> list;

    /**
     * 角色列表
     */
    private List<SecRole> roleList;

    /**
     * 拥有，0否，1是
     */
    private Integer checked;

    public interface Save {}

    public interface Update {}

    public SecOrganization() {}

    public SecOrganization(Long id) {
        this.id = id;
    }

    @Override
    public Long getNodeId() {
        return id;
    }

    @Override
    public Long getParentNodeId() {
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }

    @Override
    public void addChild(SecOrganization secOrganization) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(secOrganization);
    }

    @Override
    public List<SecOrganization> getChildren() {
        return children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SecOrganization getParent() {
        return parent;
    }

    public void setParent(SecOrganization parent) {
        this.parent = parent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SecOrganization> getList() {
        return list;
    }

    public void setList(List<SecOrganization> list) {
        this.list = list;
    }

    public List<SecRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SecRole> roleList) {
        this.roleList = roleList;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public void setChildren(List<SecOrganization> children) {
        this.children = children;
    }
}
