package com.company.admin.entity.system;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.XSGenericModel;

import javax.validation.constraints.NotBlank;

/**
 * Created by JQ棣 on 11/1/17.
 */
public class Version extends XSGenericModel {

    private Long id;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空", groups = Save.class)
    private Integer code;


    /**
     * 平台 0 安卓 1 苹果
     */
    @NotNull(message = "平台不能为空", groups = Save.class)
    private Integer platform;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "名称长度为1-255个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * url
     */
    @NotBlank(message = "url不能为空", groups = Save.class)
    private String url;

    /**
     * 状态 0:下架 1:上架
     */
    @NotNull(message = "状态不能为空", groups = Save.class)
    private Integer status;

    /**
     * 描述
     */
    @NotNull(message = "描述不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "描述长度为1-255个字符", groups = {Save.class, Update.class})
    private String desc;

    public interface Save {}

    public interface Update {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }
}
