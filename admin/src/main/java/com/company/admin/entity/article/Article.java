package com.company.admin.entity.article;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.company.admin.entity.base.BaseModel;

/**
 * 文章
 * Created by JQ棣 on 2017/10/28.
 */
public class Article extends BaseModel {

    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "标题长度为1-255个字符", groups = {Save.class, Update.class})
    private String title;

    /**
     * 类型，0富文本，1链接
     */
    private Integer type;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片链接
     */
    @URL(message = "图片链接格式错误", groups = {Save.class, Update.class})
    private String image;

    /**
     * 分类
     */
    private ArticleCategory category;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = Save.class)
    private Integer seq;

    /**
     * 状态
     */
    private Integer display;

    /**
     * 锁定，0否，1是
     */
    private Integer lock;

    /**
     * 简介
     */
    private String intro;

    /**
     * seo标题
     */
    private String seoTitle;

    /**
     * seo关键字
     */
    private String seoKeywords;

    /**
     * seo描述
     */
    private String seoDescription;

    /**
     * 是否热门，0否，1是
     */
    private Integer hot;

    /**
     * seo描述
     */
    private String author;

    /**
     * 分类列表
     */
    private List<String> cateList;

    public interface Save {}

    public interface Update {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArticleCategory getCategory() {
        return category;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public void setCategory(ArticleCategory category) {
        this.category = category;
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

    public List<String> getCateList() {
        return cateList;
    }

    public void setCateList(List<String> cateList) {
        this.cateList = cateList;
    }
}
