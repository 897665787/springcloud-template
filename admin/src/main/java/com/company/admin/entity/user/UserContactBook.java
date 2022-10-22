package com.company.admin.entity.user;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 通讯录
 * Created by wjc on 2018/11/19.
 */
@Accessors(chain = true)
@Getter
@Setter
public class UserContactBook extends BaseModel {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 姓名
	 */
	@NotBlank(message = "姓名不能为空", groups = Save.class)
	@Length(min = 1, max = 100, message = "姓名长度为1-100个字符",groups = {Save.class, Update.class})
	private String name;

	/**
	 * 手机号码
	 */
	@NotBlank(message = "手机号码不能为空", groups = Save.class)
	@Length(min = 1, max = 20, message = "手机号码长度为1-20个字符",groups = {Save.class, Update.class})
	private String mobile;

	/**
	 * 备注
	 */
	@Length(max = 65535, message = "备注长度最多为65535个字符",groups = {Save.class, Update.class})
	private String remark;

    /**
     * 序号,页面显示辅助字段
     */
    private Integer number;

	private List<UserContactBook> contactBookList;

    public UserContactBook() {
    }

    public UserContactBook(String userId) {
		this.userId = userId;
	}

	public interface Save {}

	public interface Update {}
}
