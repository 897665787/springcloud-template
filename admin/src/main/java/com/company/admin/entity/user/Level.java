package com.company.admin.entity.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.company.admin.entity.base.BaseModel;

/**
 * 等级称号
 * Created by wjc on 2018/06/21.
 */
public class Level extends BaseModel {

    /**
     * 编号
     */
    private Long id;

    /**
     * 等级
     */
    @NotNull(message = "等级不能为空", groups = {Save.class, Update.class})
    @Range(min = 1, message = "等级必须大于0", groups = {Save.class, Update.class})
    private Integer level;

    /**
     * 称号
     */
    @NotBlank(message = "称号不能为空", groups = {Save.class, Update.class})
    @Length(min = 1, max = 50, message = "称号长度为1-50个字符", groups = {Save.class, Update.class})
    private String title;

    /**
     * 经验值
     */
    @NotNull(message = "经验值不能为空", groups = {Save.class, Update.class})
    private Long exp;

    /**
     * 颜色
     */
    @NotBlank(message = "颜色不能为空", groups = {Save.class, Update.class})
    private String color;

    public interface Save {
    }

    public interface Update {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
