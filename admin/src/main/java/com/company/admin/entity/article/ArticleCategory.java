package com.company.admin.entity.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.company.admin.entity.base.BaseModel;
import com.company.admin.tree.XSTreeable;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章分类
 * Created by xuxiaowei on 2017/10/28.
 */
public class ArticleCategory extends BaseModel implements XSTreeable<String, ArticleCategory> {

    /**
     * id
     */
    private String id;

    /**
     * 键
     */
    @NotNull(message = "键不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "键长度为1-191个字符", groups = {Save.class, Update.class})
    private String key;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "名称长度为1-255个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 预览前缀
     */
    @NotNull(message = "预览前缀不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "预览前缀长度为1-255个字符", groups = {Save.class, Update.class})
    private String prefix;

    /**
     * 图标
     */
    @Length(max = 255, message = "图标长度最多为255个字符", groups = {Save.class, Update.class})
    private String icon;

    /**
     * 父级
     */
    private ArticleCategory parent;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = Save.class)
    private Integer seq;

    /**
     * 展示，0否，1是
     */
    private Integer display;

    /**
     * 锁定，0否，1是
     */
    private Integer lock;

    /**
     * 子级列表
     */
    private List<ArticleCategory> children;

    /**
     * 分类列表
     */
    private List<ArticleCategory> list;

    public interface Save {}

    public interface Update {}


    public ArticleCategory() {
    }

    public ArticleCategory(String id) {
        this.id = id;
    }

    @JsonIgnore
    @Override
    public String getNodeId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getParentNodeId() {
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }

    @Override
    public void addChild(ArticleCategory articleCategory) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(articleCategory);
    }

    @Override
    public List<ArticleCategory> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArticleCategory getParent() {
        return parent;
    }

    public void setParent(ArticleCategory parent) {
        this.parent = parent;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    public void setChildren(List<ArticleCategory> children) {
        this.children = children;
    }

    public List<ArticleCategory> getList() {
        return list;
    }

    public void setList(List<ArticleCategory> list) {
        this.list = list;
    }
}
