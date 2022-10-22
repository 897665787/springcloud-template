package com.company.admin.entity.system;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;

/**
 * 国家
 * Created by xuxiaowei on 2018/5/3.
 */
public class Country extends BaseModel {

    /**
     * 编号
     */
    private Long id;

    /**
     * 中文简称
     */
    @NotNull(message = "中文简称不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "中文简称长度为1-191个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 英文简称
     */
    @NotNull(message = "英文简称不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "英文简称长度为1-191个字符", groups = {Save.class, Update.class})
    private String enName;

    /**
     * 电话区号
     */
    @NotNull(message = "电话区号不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "电话区号长度为1-255个字符", groups = {Save.class, Update.class})
    private String phoneCode;

    /**
     * 数字代码
     */
    @NotNull(message = "数字代码不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "数字代码长度为1-255个字符", groups = {Save.class, Update.class})
    private String code;

    /**
     * 两字符简称
     */
    @NotNull(message = "两字符简称不能为空", groups = Save.class)
    @Length(min = 2, max = 2, message = "两字符简称长度为2个字符", groups = {Save.class, Update.class})
    private String code2;

    /**
     * 三字符简称
     */
    @NotNull(message = "三字符简称不能为空", groups = Save.class)
    @Length(min = 3, max = 3, message = "三字符简称长度为3个字符", groups = {Save.class, Update.class})
    private String code3;

    /**
     * 中文全称
     */
    @NotNull(message = "中文全称不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "中文全称长度为1-191个字符", groups = {Save.class, Update.class})
    private String fullName;

    /**
     * 英文全称
     */
    @NotNull(message = "英文全称不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "英文全称长度为1-191个字符", groups = {Save.class, Update.class})
    private String enFullName;

    /**
     * 大洲，0-亚洲，1-欧洲，2-非洲，3-大洋洲，4-北美洲，5-南美洲，6-南极洲
     */
    private Integer land;

    /**
     * 状态，0关闭，1开通
     */
    private Integer status;

    @NotNull(message = "顺序不能为空", groups = Save.class)
    private Long seq;

    /**
     * 拼音
     */
    private String pinyin;

    public interface Save {}

    public interface Update {}

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEnFullName() {
        return enFullName;
    }

    public void setEnFullName(String enFullName) {
        this.enFullName = enFullName;
    }

    public Integer getLand() {
        return land;
    }

    public void setLand(Integer land) {
        this.land = land;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
