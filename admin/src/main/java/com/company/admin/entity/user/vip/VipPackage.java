package com.company.admin.entity.user.vip;

import com.company.admin.jackson.annotation.AutoDesc;
import com.company.framework.jackson.annotation.FormatNumber;
import com.company.admin.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 会员套餐
 * Created by JQ棣 on 2018/11/14.
 */
@Accessors(chain = true)
@Getter
@Setter
public class VipPackage extends BaseModel {

	/**
	 * ID，此处用字符串的原因是苹果后台需要配置商品ID
	 */
	@NotBlank(message = "ID不能为空", groups = Save.class)
	@Length(min = 1, max = 16, message = "ID长度为1-16个字符",groups = {Save.class, Update.class})
	private String id;

	/**
	 * 名称
	 */
	@NotBlank(message = "名称不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "名称长度为1-32个字符",groups = {Save.class, Update.class})
	private String name;

	/**
	 * 金额
	 */
	@NotNull(message = "金额不能为空", groups = Save.class)
	@DecimalMin(value = "0", message = "金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@Digits(integer = 7, fraction = 2, message = "金额范围为0-9999999.99，至多2位小数", groups = {Save.class, Update.class})
	@FormatNumber(pattern = "0.##")
	private BigDecimal fee;

	/**
	 * 时长，单位天
	 */
	@NotNull(message = "时长不能为空", groups = Save.class)
	@Range(min = 1, message = "时长至少为1", groups = {Save.class, Update.class})
	private Integer duration;

	/**
	 * 状态，0-下架，1-上架
	 */
	@NotNull(message = "状态不能为空", groups = Save.class)
	@Range(min = 0, message = "状态至少为0", groups = {Save.class, Update.class})
	@AutoDesc({"0:下架", "1:上架"})
	private Integer status;

	/**
	 * 顺序
	 */
	@NotNull(message = "顺序不能为空", groups = Save.class)
	@Range(min = 0, message = "顺序至少为0", groups = {Save.class, Update.class})
	private Integer seq;

	/**
	 * 平台，1-iOS，2-Android
	 */
	@NotNull(message = "平台不能为空", groups = Save.class)
	@Range(min = 0, message = "平台至少为0", groups = {Save.class, Update.class})
	@AutoDesc({"1:iOS", "2:Android"})
	private Integer platform;

	public interface Save {}

	public interface Update {}
}
