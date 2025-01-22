package com.company.admin.entity.user.vip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.company.admin.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 会员历史
 * Created by JQ棣 on 2018/11/15.
 */
@Accessors(chain = true)
@Getter
@Setter
public class VipHistory extends BaseModel {

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "ID长度为1-32个字符",groups = {Save.class, Update.class})
	private String id;

	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "用户ID长度为1-32个字符",groups = {Save.class, Update.class})
	private String userId;

	/**
	 * 套餐ID
	 */
	@NotBlank(message = "套餐ID不能为空", groups = Save.class)
	@Length(min = 1, max = 16, message = "套餐ID长度为1-16个字符",groups = {Save.class, Update.class})
	private String packageId;

	/**
	 * 套餐描述
	 */
	@NotBlank(message = "套餐描述不能为空", groups = Save.class)
	@Length(min = 1, max = 256, message = "套餐描述长度为1-256个字符",groups = {Save.class, Update.class})
	private String packageDesc;

	/**
	 * 套餐时长，单位天
	 */
	@NotNull(message = "套餐时长不能为空", groups = Save.class)
	@Range(min = 1, message = "套餐时长至少为1", groups = {Save.class, Update.class})
	private Integer packageDuration;

	/**
	 * 变化前的会员到期时间
	 */
	@NotNull(message = "变化前的会员到期时间不能为空", groups = Save.class)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date changeBefore;

	/**
	 * 变化后的会员到期时间
	 */
	@NotNull(message = "变化后的会员到期时间不能为空", groups = Save.class)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date changeAfter;

	/**
	 * 完成时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date completeTime;

	private String creatorId;

	private String creator;

	public interface Save {}

	public interface Update {}
}
