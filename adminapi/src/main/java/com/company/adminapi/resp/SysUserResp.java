package com.company.adminapi.resp;

import com.company.framework.enums.DesensitizedType;
import com.company.framework.jackson.annotation.Sensitive;
import lombok.Data;

/**
 * 用户信息
 *
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysUserResp extends com.company.system.api.response.SysUserResp{
	/**
	 * 用户邮箱
	 */
	@Sensitive(DesensitizedType.EMAIL) // 需要对字段做脱敏，继承原类覆盖原属性并增加注解
	private String email;

	/**
	 * 手机号码
	 */
	@Sensitive(DesensitizedType.MOBILE) // 需要对字段做脱敏，继承原类覆盖原属性并增加注解
	private String phonenumber;
}
