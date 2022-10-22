package com.company.admin.entity.system;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.company.admin.entity.base.BaseModel;

/**
 * 区县
 * Created by wjc on 2018/05/30.
 */
public class District extends BaseModel {

    /**
     * id
     */
    @NotNull(message = "编号不能为空", groups = Save.class)
    @Range(min = 1L, message = "编号范围为1-9223372036854775807", groups = {Save.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 50, message = "名称长度为1-50个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 状态，0关闭，1开通
     */
    private Integer status;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = Save.class)
    @Range(min = 0, message = "顺序至少为0", groups = {Save.class, Update.class})
    private Integer seq;

    /**
     * 拥有，0否，1是
     */
    private Integer checked;

    private City city;

    /**
     * 父级id
     */
    private Long pId;

    public District() {
    }

    public District(Long id) {
        this.id = id;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }
}
