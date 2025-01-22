package com.company.admin.entity.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;
import com.company.common.jackson.annotation.FormatNumber;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户
 * Created by JQ棣 on 2017/10/26.
 */
@Accessors(chain = true)
@Getter
@Setter
public class User extends BaseModel {

    /**
     * id
     */
    private String id;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空", groups = {RegisterLoginUnify.class, MobileUser.class, MobilePassword.class,
            UpdateMobile.class, ForgetPassword.class})
    @Pattern(regexp = "^(12[0-9]|13[0-9]|14[0-1,4-9]|15[0-9]|16[5-6]|17[0-8]|18[0-9]|19[89])\\d{8}$",
            message = "手机号格式错误",
            groups = {RegisterLoginUnify.class, MobileUser.class, MobilePassword.class, UpdateMobile.class,
                    ForgetPassword.class})
    private String mobile;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空", groups = {UsernamePassword.class, UpdateUsername.class})
    @Length(min = 1, max = 32, message = "用户名长度为1-32个字符", groups = {UsernamePassword.class, UpdateUsername.class})
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空", groups = {UsernamePassword.class, MobilePassword.class, UpdatePassword.class,
            ForgetPassword.class})
    @Length(min = 6, max = 16, message = "密码长度为6-16个字符", groups = {UsernamePassword.class, MobilePassword.class,
            UpdatePassword.class, ForgetPassword.class})
    private String password;

    /**
     * 旧密码
     */
    @Length(max = 16, message = "旧密码长度最多为16个字符", groups = {UpdatePassword.class})
    private String oldPassword;

    /**
     * 微信unionid
     */
    @NotBlank(message = "微信unionid不能为空", groups = {RegisterByWeChat.class})
    private String unionid;

    /**
     * 微信openid
     */
    @NotBlank(message = "微信openid不能为空", groups = {RegisterByWeChat.class})
    private String openid;

    /**
     * 状态，0-冻结，1-正常
     */
    @NotNull(message = "状态不能为空")
    @AutoDesc({"0:冻结", "1:正常"})
    private Integer status;

    /**
     * 类型，0-用户，1-游客，10-超级管理员，11-员工
     */
    @AutoDesc({"0:用户", "1:游客", "10:超级管理员", "11:员工"})
    private Integer type;

    /**
     * 真实姓名
     */
    //@NotNull(message = "真实姓名不能为空")
    @Length(min = 1, max = 32, message = "真实姓名长度为1-32个字符")
    private String realname;

    /**
     * 客户ID，唯一
     */
    private String customerId;

    /**
     * 头像
     */
    @URL(message = "头像地址格式错误", groups = {Update.class})
    private String avatar;

    /**
     * 手机号区号
     */
    private String areaCode;

    /**
     * 姓名
     */
    @Length(min = 1, max = 32, message = "昵称长度为1-32个字符", groups = {Update.class})
    private String nickname;

    /**
     * 数字验证码
     */
    @NotNull(message = "数字验证码不能为空", groups = {loginWithRegister.class, RegisterByWeChat.class})
    @Valid
    private DigitalCaptcha digitalCaptcha;

    /**
     * 微信开放平台票据
     */
    private String weChatCode;

    /**
     * 微信开放平台访问令牌
     */
    private String weChatAccessToken;

    /**
     * 设备ID，唯一
     */
    @NotBlank(message = "设备ID不能为空", groups = {Tourist.class})
    private String deviceId;

    /**
     * 操作类型，1-管理平台，2-注册登录一体化，3-微信授权，4-手机号，5-账号密码，6-手机号密码，7-游客
     */
    @NotNull(message = "操作类型不能为空", groups = {Operation.class})
    @AutoDesc({"1:管理平台", "2:注册登录一体化", "3:微信授权", "4:手机号", "5:账号密码", "6:手机号密码", "7:游客"})
    private Integer operationType;

    /**
     * 操作平台，1-Admin，2-iOS，3-Android，4-H5
     */
    @NotNull(message = "操作平台不能为空", groups = {Operation.class})
    @AutoDesc({"1:Admin", "2:iOS", "3:Android", "4:H5"})
    private Integer operationPlatform;

    /**
     * 操作版本号
     */
    @Length(min = 1, max = 8, message = "操作版本号长度为1-8个字符", groups = {Operation.class})
    private String operationVersion;

    /**
     * 操作IP
     */
    private String operationIp;

    /**
     * 性别，1-男，2-女
     */
    @AutoDesc({"1:男", "2:女"})
    private Integer sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 身份证号码
     */
    private String identityNumber;

    /**
     * 个性签名
     */
    @Length(max = 256, message = "签名长度最多为256个字符", groups = {Update.class})
    private String personalSignature;

    /**
     * 绑定微信时间
     */
    private Date bindWeChatTime;
    
    /**
     * 经验值
     */
    private Long exp;
    
    /**
     * 积分
     */
    private Long credit;
    
    /**
     * 等级
     */
    private Integer level;

    /**
     * 用户地址列表
     */
    private List<UserAddress> addressList;

    /**
     * iOS钱包
     */
    private BigDecimal iosWallet;

    /**
     * android钱包
     */
    private BigDecimal androidWallet;

    /**
     * 平台，1-iOS，2-Android
     */
    @NotNull(message = "平台不能为空", groups = UpdateWallet.class)
    private Integer platform;

    /**
     * 钱包金额
     */
    @NotNull(message = "金额（元）不能为空", groups = UpdateWallet.class)
    @DecimalMin(value = "-9999999.99", message = "金额（元）范围为-9999999.99-9999999.99", groups = UpdateWallet.class)
    @Digits(integer = 7, fraction = 2, message = "金额（元）范围为-9999999.99-9999999.99，至多两位小数", groups = UpdateWallet.class)
    @FormatNumber(pattern = "0.##")
    private BigDecimal changeFee;

    /**
     * 是否为会员，0-否，1-是
     */
    private Integer vip;

    /**
     * 会员到期时间
     */
    private Date vipExpire;

    /**
     * 会员时长，单位天
     */
    @NotNull(message = "会员时长不能为空", groups = UpdateVip.class)
    private Integer changeDuration;

    /**
     * 邀请码，6位字母和数字的组合
     */
    private String inviteCode;

    /**
     * 邀请者ID
     */
    private String inviterId;

    public interface loginWithRegister {}

    public interface RegisterByWeChat {}

    public interface MobileNotBlank {}

    public interface Operation {}

    public interface RegisterLoginUnify {}

    public interface UsernamePassword {}

    public interface MobileUser {}

    public interface MobilePassword {}

    public interface Tourist {}

    public interface WeChatUser {}

    public interface UpdateUsername {}

    public interface UpdateMobile {}

    public interface UpdatePassword {}

    public interface ForgetPassword {}

    public interface Update {}

    public interface UpdateWallet {}

    public interface UpdateVip {}

    public User() {}

    public User(String id) {
        this.id = id;
    }

    /**
     * 已关注，0-否，1-是
     */
    private Integer followed;
}
