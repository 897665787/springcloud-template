package com.company.admin.entity.user;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;
import com.company.common.jackson.annotation.FormatNumber;

/**
 * 用户收货地址
 * Created by JQ棣 on 2018/05/31.
 */
public class UserAddress extends BaseModel {

    /**
     * 用户地址id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 联系人名称
     */
    @NotBlank(message = "联系人不能为空", groups = Save.class)
    @Length(min = 1, max = 20, message = "联系人必须为1-20个字符", groups = {Save.class, Update.class})
    private String contacts;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = Save.class)
    private String mobile;

    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空", groups = Save.class)
    @Length(min = 1, max = 20, message = "省份长度为1-20个字符", groups = {Save.class, Update.class})
    private String province;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空", groups = Save.class)
    @Length(min = 1, max = 20, message = "城市长度为1-20个字符", groups = {Save.class, Update.class})
    private String city;

    /**
     * 行政区
     */
    @NotBlank(message = "行政区不能为空", groups = Save.class)
    @Length(min = 1, max = 20, message = "行政区长度为1-20个字符", groups = {Save.class, Update.class})
    private String district;


    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空", groups = Save.class)
    @Length(min = 1, max = 100, message = "详细地址长度为1-100个字符", groups = {Save.class, Update.class})
    private String address;

    /**
     * 地区编码
     */
    @NotNull(message = "地区编码不能为空", groups = Save.class)
    private Long districtId;

    /**
     * 城市编码
     */
    @NotNull(message = "地区编码不能为空", groups = Save.class)
    private Long cityId;


    /**
     * 省份编码
     */
    @NotNull(message = "地区编码不能为空", groups = Save.class)
    private Long provinceId;


    /**
     * 经度
     */
    @DecimalMin(value = "0", message = "经度范围为0-9999.999999999999，至多12位小数", groups = {Save.class, Update.class})
    @Digits(integer = 16, fraction = 12, message = "经度范围为0-9999.999999999999，至多12位小数", groups = {Save.class, Update.class})
    @FormatNumber(pattern = "0.############")
    private BigDecimal lng;

    /**
     * 纬度
     */
    @DecimalMin(value = "0", message = "纬度范围为0-9999.999999999999，至多12位小数", groups = {Save.class, Update.class})
    @Digits(integer = 16, fraction = 12, message = "纬度范围为0-9999.999999999999，至多12位小数", groups = {Save.class, Update.class})
    @FormatNumber(pattern = "0.############")
    private BigDecimal lat;

    /**
     * 是否默认，0：否；1：是；
     */
    private Integer major;

    /**
     * 所在地区，由 省 市 区 三级拼接的字段
     */
    private String region;

    public interface Save {
    }

    public interface Update {
    }

    public UserAddress() {
    }

    public UserAddress(Long id) {
        this.id = id;
    }

    public UserAddress(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
