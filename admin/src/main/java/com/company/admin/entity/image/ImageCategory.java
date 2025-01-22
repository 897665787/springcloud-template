package com.company.admin.entity.image;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.entity.system.Dictionary;
import com.company.admin.tree.XSTreeable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 图片分类
 * Created by JQ棣 on 2017/10/23.
 */
public class ImageCategory extends BaseModel implements XSTreeable<String, ImageCategory> {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "名称长度为1-255个字符",groups = {Save.class, Update.class})
    private String name;

    /**
     * 父级
     */
    private ImageCategory parent;

    /**
     * 键
     */
    @NotBlank(message = "键不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "键长度为1-191个字符",groups = {Save.class, Update.class})
    private String key;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = Save.class)
    @Range(min = 0, message = "顺序至少为0", groups = {Save.class, Update.class})
    private Integer seq;

    /**
     * 状态，0-下架，1-上架
     */
    @NotNull(message = "状态不能为空", groups = Save.class)
    @Range(min = 0, message = "状态至少为0", groups = {Save.class, Update.class})
    private Integer status;

    /**
     * 图片
     */
    @URL(message = "图片链接错误", groups = {Save.class, Update.class})
    private String image;

    /**
     * 锁定，0-否，1-是
     */
    private Integer lock;

    /**
     * 跳转类型数据字典列表
     */
    private List<Dictionary> jumpTypeDictDataList;

    /**
     * 子级列表
     */
    private List<ImageCategory> children;

    /**
     * 分类列表
     */
    private List<ImageCategory> list;

    public interface Save {}

    public interface Update {}

    public ImageCategory() {}

    public ImageCategory(String id) {
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
    public void addChild(ImageCategory imageCategory) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(imageCategory);
    }

    @Override
    public List<ImageCategory> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageCategory getParent() {
        return parent;
    }

    public void setParent(ImageCategory parent) {
        this.parent = parent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    public List<Dictionary> getJumpTypeDictDataList() {
        return jumpTypeDictDataList;
    }

    public void setJumpTypeDictDataList(List<Dictionary> jumpTypeDictDataList) {
        this.jumpTypeDictDataList = jumpTypeDictDataList;
    }

    public void setChildren(List<ImageCategory> children) {
        this.children = children;
    }

    public List<ImageCategory> getList() {
        return list;
    }

    public void setList(List<ImageCategory> list) {
        this.list = list;
    }
}
