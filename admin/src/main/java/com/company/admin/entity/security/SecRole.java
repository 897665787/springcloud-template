package com.company.admin.entity.security;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;

/**
 * 系统角色
 * Created by JQ棣 on 2017/10/23.
 */
public class SecRole extends BaseModel {

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
     * 状态，0禁用，1正常
     */
    private Integer status;

    /**
     * 描述
     */
    @Length(max = 255, message = "描述长度最多为255个字符", groups = {Save.class, Update.class})
    private String desc;

    /**
     * 拥有，0否，1是
     */
    private Integer checked;

    /**
     * 资源列表
     */
    private List<SecResource> resourceList;

    public interface Save {}

    public interface Update {}

    public SecRole() {}

    public SecRole(Long id) {
        this.id = id;
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

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public List<SecResource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<SecResource> resourceList) {
        this.resourceList = resourceList;
    }
}
