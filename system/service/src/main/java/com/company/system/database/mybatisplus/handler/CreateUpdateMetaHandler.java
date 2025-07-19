package com.company.system.database.mybatisplus.handler;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.company.framework.context.HttpContextUtil;

@Component
public class CreateUpdateMetaHandler implements MetaObjectHandler {
	private static final Integer DEFAULT_CREATOR_UID = 0;

	@Override
	public void insertFill(MetaObject metaObject) {
		Object createTime = this.getFieldValByName("createTime", metaObject);
		if (createTime == null) {
			this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
		}
		Object updateTime = this.getFieldValByName("updateTime", metaObject);
		if (updateTime == null) {
			this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		}

		Integer sysUserId = HttpContextUtil.currentUserIdInt();// 获取当前登录用户
		if (sysUserId == null) {
			sysUserId = DEFAULT_CREATOR_UID;
		}
		
		Object createBy = this.getFieldValByName("createBy", metaObject);
		if (createBy == null) {
			this.setFieldValByName("createBy", sysUserId, metaObject);
		}
		Object updateBy = this.getFieldValByName("updateBy", metaObject);
		if (updateBy == null) {
			this.setFieldValByName("updateBy", sysUserId, metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		
		Integer sysUserId = HttpContextUtil.currentUserIdInt();// 获取当前登录用户
		if (sysUserId == null) {
			sysUserId = DEFAULT_CREATOR_UID;
		}
		
		Object updateBy = this.getFieldValByName("updateBy", metaObject);
		if (updateBy == null) {
			/**
			 * <pre>
			 * 问题1.这里如果不判断直接就更新，可能会导致代码设置的updateBy生效，系统异步存储的都是0
			 * 问题2.如果实体类是先查询出来再直接更新的，那么会导致updateBy不变
			 * 
			 * 取舍：代码生成的大部分都不会从数据库先查出来再更新，但又需求在异步过程主动设置更新人，所以取解决问题1。问题2的话建议新建实体类更新，不要用查询的实体类直接更新
			 * </pre>
			 */
			this.setFieldValByName("updateBy", sysUserId, metaObject);
		}
	}
}