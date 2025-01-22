package com.company.admin.entity.system;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典分类
 * @author JQ棣
 * @date 2018/9/22
 */
@Getter
@Setter
public class DictionaryCategory extends BaseModel {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = {Update.class})
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空", groups = {Save.class})
    @Length(min = 1, max = 32, message = "名称长度为1-32个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 键
     */
    @NotNull(message = "键不能为空", groups = {Save.class})
    @Length(min = 1, max = 32, message = "键长度为1-32个字符", groups = {Save.class, Update.class})
    private String key;

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

    public interface Save {}

    public interface Update {}
}
