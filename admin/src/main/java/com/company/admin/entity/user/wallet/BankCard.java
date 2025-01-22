package com.company.admin.entity.user.wallet;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 银行卡
 * Created by JQ棣 on 2018/11/21.
 */
@Accessors(chain = true)
@Getter
@Setter
public class BankCard extends BaseModel {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 卡号
	 */
	@NotBlank(message = "卡号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "卡号长度为1-32个字符", groups = {Save.class, Update.class})
	private String cardNumber;

	/**
	 * 开户人
	 */
	@Length(max = 32, message = "开户人长度为1-32个字符", groups = {Save.class, Update.class})
	private String owner;

	/**
	 * 身份证号码
	 */
	@Length(max = 32, message = "身份证号码长度为1-32个字符", groups = {Save.class, Update.class})
	private String identityNumber;

	/**
	 * 手机号
	 */
	@Pattern(regexp = "^(\\d{0})|((13[0-9]|14[01456789]|15[0-9]|16[56]|17[01235678]|18[0-9]|19[89])\\d{8})$",
			message = "手机号格式错误", groups = {Save.class, Update.class})
	private String mobile;

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 银行信息
	 */
	private String bankInfo;

	public interface Save {}

	public interface Update {}
}
