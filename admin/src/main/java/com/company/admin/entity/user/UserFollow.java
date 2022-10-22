package com.company.admin.entity.user;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户关注 Created by JQ棣 on 2018/11/10.
 */
@Accessors(chain = true)
@Getter
@Setter
public class UserFollow extends BaseModel {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 被关注者ID
	 */
	@NotBlank(message = "被关注者ID不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "被关注者ID长度为1-32个字符", groups = Save.class)
	private String targetId;

	public interface Save {}

	@NotNull(message = "被关注者ID列表不能为空", groups = BatchSave.class)
	@Size(min = 1, message = "被关注者ID列表不能为空", groups = BatchSave.class)
	private List<String> targetIdList;
	
	public interface BatchSave {}
}
