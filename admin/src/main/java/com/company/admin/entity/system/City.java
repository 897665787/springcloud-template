package com.company.admin.entity.system;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.company.admin.entity.base.BaseModel;

/**
 * 城市
 * Created by xuxiaowei on 2017/11/13.
 */
public class City extends BaseModel {

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

    private Province province;

    private List<District> children;

    /**
     * 父级id
     */
    private Long pId;

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

    public String getName() {
        return name;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }


    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public List<District> getChildren() {
        return children;
    }

    public void setChildren(List<District> children) {
        this.children = children;
    }
}
