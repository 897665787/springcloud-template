package com.company.admin.entity.user;

import javax.validation.constraints.Pattern;

import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;

/**
 * 数字验证码
 * Created by JQ棣 on 2017/8/1.
 */
public class DigitalCaptcha extends BaseModel {

    /**
     * id
     */
    private Long id;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {Save.class, Verify.class})
    @Pattern(regexp = "^\\s*\\+?\\s*(\\(\\s*\\d+\\s*\\)|\\d+)(\\s*-?\\s*(\\(\\s*\\d+\\s*\\)|\\s*\\d+\\s*))*\\s*$",
            message = "手机号格式错误", groups = {Save.class, Verify.class})
    private String mobile;

    /**
     * 类型，1-注册登录一体化，2-注册，3-登录，4-更新密码
     */
    private Integer type;

    /**
     * 内容
     */
    @NotBlank(message = "数字验证码不能为空", groups = {User.loginWithRegister.class, User.RegisterByWeChat.class, Verify.class})
    @Pattern(regexp = "^[0-9]{6}$", message = "数字验证码格式为6位数字", groups = {User.loginWithRegister.class,
            User.RegisterByWeChat.class, Verify.class})
    private String content;

    /**
     * 手机号区号
     */
    private String areaCode;

    /**
     * 通知方式，0-文字短信，1-语音短信
     */
    private Integer channel;

    public interface Save {}

    public interface Verify {}

    public DigitalCaptcha() {
    }

    public DigitalCaptcha(String mobile, Integer type, String content) {
        this.mobile = mobile;
        this.type = type;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
