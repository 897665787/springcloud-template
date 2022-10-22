package com.company.admin.entity.marketing;

import com.company.admin.jackson.annotation.AutoDesc;
import com.company.admin.entity.base.BaseModel;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 热搜词
 * Created by wjc on 2018/11/07.
 */
public class HotWord extends BaseModel {

	/**
	 * 编号
	 */
	private Long id;

	/**
	 * 名称
	 */
	@NotBlank(message = "名称不能为空", groups = Save.class)
	@Length(min = 1, max = 50, message = "名称长度为1-50个字符",groups = {Save.class, Update.class})
	private String name;

	/**
	 * 状态, 0-关闭, 1-启用
	 */
	@AutoDesc({"0:关闭","1:启用"})
	private Integer status;

	/**
	 * 顺序
	 */
	@NotNull(message = "顺序不能为空", groups = Save.class)
	private Integer seq;

	public interface Save {}

	public interface Update {}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSeq() {
		return seq;
	}
	
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
