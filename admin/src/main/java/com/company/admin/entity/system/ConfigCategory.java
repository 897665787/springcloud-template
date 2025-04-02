package com.company.admin.entity.system;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.tree.XSTreeable;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置分类
 * @author JQ棣
 * @date 2018/9/22
 */
@Getter
@Setter
public class ConfigCategory extends BaseModel implements XSTreeable<Long, ConfigCategory> {

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
     * 父级ID
     */
    private Long parentId;

    /**
     * 父级名称
     */
    private String parentName;

    /**
     * 子级列表
     */
    private List<ConfigCategory> children;

    /**
     * 配置列表
     */
    private List<Config> configs;

    public interface Save {}

    public interface Update {}

    @Override
    public Long getNodeId() {
        return id;
    }

    @Override
    public Long getParentNodeId() {
        return parentId;
    }

    @Override
    public void addChild(ConfigCategory configCategory) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(configCategory);
    }

    @Override
    public List<ConfigCategory> getChildren() {
        return children;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }
}
