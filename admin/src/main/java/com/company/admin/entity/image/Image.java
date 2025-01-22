package com.company.admin.entity.image;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.entity.system.Dictionary;

/**
 * 图片
 * Created by JQ棣 on 2017/10/25.
 */
public class Image extends BaseModel {

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
     * 图片
     */
    @NotBlank(message = "图片不能为空", groups = Save.class)
    @Length(min = 1, message = "图片不能为空", groups = {Save.class, Update.class})
    @URL(message = "图片链接格式错误", groups = {Save.class, Update.class})
    private String image;

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
     * 分类
     */
    private ImageCategory category;

    /**
     * 跳转类型，0-网页链接，1-App内页
     */
    @NotNull(message = "跳转类型不能为空", groups = Save.class)
    @Range(min = 0, message = "跳转类型至少为0", groups = {Save.class, Update.class})
    private Integer jumpType;

    /**
     * 网页链接
     */
    @Length(max = 65535, message = "网页链接长度最多为65535个字符",groups = {Save.class, Update.class})
    private String webLink;

    /**
     * App内页字典
     */
    @Range(min = 0, message = "App内页字典至少为0", groups = {Save.class, Update.class})
    private Integer appInnerPageDict;

    /**
     * 页面参数
     */
    @Length(max = 255, message = "页面参数长度最多为255个字符",groups = {Save.class, Update.class})
    private String pageParam;

    /**
     * 跳转类型数据字典列表
     */
    private List<Dictionary> jumpTypeDictDataList;

    /**
     * 分类列表
     */
    private List<String> categoryList;

    public interface Save {}

    public interface Update {}

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public ImageCategory getCategory() {
        return category;
    }

    public void setCategory(ImageCategory category) {
        this.category = category;
    }

    public Integer getJumpType() {
        return jumpType;
    }

    public void setJumpType(Integer jumpType) {
        this.jumpType = jumpType;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public Integer getAppInnerPageDict() {
        return appInnerPageDict;
    }

    public void setAppInnerPageDict(Integer appInnerPageDict) {
        this.appInnerPageDict = appInnerPageDict;
    }

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public List<Dictionary> getJumpTypeDictDataList() {
        return jumpTypeDictDataList;
    }

    public void setJumpTypeDictDataList(List<Dictionary> jumpTypeDictDataList) {
        this.jumpTypeDictDataList = jumpTypeDictDataList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
