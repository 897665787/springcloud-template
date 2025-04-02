package com.company.admin.entity.system;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.company.admin.entity.base.BaseModel;

/**
 * 省份
 * Created by JQ棣 on 2017/11/1.
 */
public class Province extends BaseModel {

    /**
     * id
     */
    @NotNull(message = "编号不能为空", groups = Save.class)
    @Range(min = 1L, message = "编号范围为1-9223372036854775807", groups = {Save.class})
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "名称长度为1-191个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 状态，0关闭，1开通
     */
    private Integer status;

    @NotNull(message = "顺序不能为空", groups = Save.class)
    private Long seq;

    /**
     * 拥有，0否，1是
     */
    private Integer checked;

    private List<City> children;

    public interface Save {}

    public interface Update {}

    public Province() {
    }

    public Province(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<City> getChildren() {
        return children;
    }

    public void setChildren(List<City> children) {
        this.children = children;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}