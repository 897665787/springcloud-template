package com.company.admin.entity.system;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典
 * @author xxw
 * @date 2018/9/22
 */
@Getter
@Setter
public class Dictionary extends BaseModel {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = {Update.class})
    private Long id;

    /**
     * 键
     */
    @NotNull(message = "键不能为空", groups = {Save.class})
    @Length(min = 1, max = 32, message = "键长度为1-32个字符", groups = {Save.class, Update.class})
    private String key;

    /**
     * 值
     */
    @NotNull(message = "值不能为空", groups = {Save.class})
    @Length(min = 1, message = "值长度至少为1个字符", groups = {Save.class, Update.class})
    private String value;

    /**
     * 备注
     */
    @Length(max = 256, message = "备注长度最多为256个字符", groups = {Save.class, Update.class})
    private String comment;

    /**
     * 锁定，0-否，1-是
     */
    @AutoDesc({"0:否", "1:是"})
    private Integer lock;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = {Save.class})
    private Integer seq;

    /**
     * 状态，0-下架，1-上架
     */
    @NotNull(message = "状态不能为空", groups = {Save.class})
    @AutoDesc({"0:下架", "1:上架"})
    private Integer status;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空", groups = {Save.class})
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类键
     */
    private String categoryKey;

    /**
     * 图标
     */
    @URL(message = "图标格式错误", groups = {Save.class, Update.class})
    private String icon;

    /**
     * 颜色代码
     */
    @Length(max = 32, message = "颜色代码长度最多为32个字符", groups = {Save.class, Update.class})
    private String color;

    private Integer checked;

    public interface Save {}

    public interface Update {}
}
