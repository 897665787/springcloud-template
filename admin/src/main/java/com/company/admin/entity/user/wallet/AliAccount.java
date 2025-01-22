package com.company.admin.entity.user.wallet;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 支付宝
 * Created by JQ棣 on 2018/11/21.
 */
@Accessors(chain = true)
@Getter
@Setter
public class AliAccount extends BaseModel {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 真实姓名
	 */
	@NotBlank(message = "真实姓名不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "真实姓名长度为1-32个字符",groups = {Save.class, Update.class})
	private String name;

	/**
	 * 支付宝账号
	 */
	@NotBlank(message = "支付宝账号不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "支付宝账号长度为1-32个字符",groups = {Save.class, Update.class})
	private String account;

	public interface Save {}

	public interface Update {}
}
