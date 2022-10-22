package com.company.admin.entity.system;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置
 * @author xxw
 * @date 2018/9/22
 */
@Getter
@Setter
public class Config extends BaseModel {

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
     * 值
     */
    @NotNull(message = "值不能为空", groups = {Save.class})
    @Length(min = 1, message = "值长度至少为1个字符", groups = {Save.class, Update.class})
    private String value;

    /**
     * 类型，1-数值，2-布尔值，3-字符串，4-文本，5-图片
     */
    @NotNull(message = "类型不能为空", groups = {Save.class})
    @AutoDesc({"1:数值", "2:布尔值", "3:字符串", "4:文本", "5:图片"})
    private Integer type;

    /**
     * 备注
     */
    @Length(max = 256, message = "备注长度最多为256个字符", groups = {Save.class, Update.class})
    private String comment;

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

    public interface Save {}

    public interface Update {}
}
